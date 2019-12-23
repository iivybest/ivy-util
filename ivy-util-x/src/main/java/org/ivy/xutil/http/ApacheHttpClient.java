/**
 *
 */
package org.ivy.xutil.http;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.ivy.util.common.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * @ClassName: ApacheHttpClient
 * @author: ivybest imiaodev@163.com
 * @date: 2018年11月6日 上午11:02:06
 * @Description: TODO(用一句话描述该文件做什么)
 */
public class ApacheHttpClient {
    // ==== step_2 静态内部类持有外部类的静态对象
    private static class ApacheHttpClientHolder {
        static {
            System.out.println("========》 类级内部类 加载。。。。。。。。。。。");
        }

        private static ApacheHttpClient instance = new ApacheHttpClient();
    }

    // slf4j logger
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 连接池管理器
    private HttpClientConnectionManager httpClientConnectionManager;
    // 请求配置参数
    private RequestConfig requestConfig;
    // 默认编码
    private String defaultEncoding;
    // 从连接池获取连接超时时间
    private Integer connectionRequestTimeout;
    // socket连接超时时间
    private Integer connectTimeout;
    // socket读取数据超时时间
    private Integer socketTimeout;
    // http client 用于发送http请求
    private CloseableHttpClient client;

    // ==== step_1 私有化构造器
    private ApacheHttpClient() {
    }

    /**
     * @Title: getInstance
     * @Description: 获得httpclient实例
     * @param args
     * @return ApacheHttpClient
     */
    // ==== step_3 提供工厂方法
    public static ApacheHttpClient getInstance() {
        return ApacheHttpClientHolder.instance.initialize();
    }

    private ApacheHttpClient initialize() {
        this.initDefaultArgs()                        // 初始化默认参数
                .buildHttpClientConnectionManager()    // 构建连接池管理器
                .buildRequestConfig()                // 构建一般请求参数
                .buildHttpClient();                    // 构建http client
        return this;
    }


    private ApacheHttpClient buildRequestConfig() {
        this.requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(this.connectionRequestTimeout)        // 从池中获取请求的时间
                .setConnectTimeout(this.connectTimeout)                        // 连接超时时间
                .setSocketTimeout(this.socketTimeout)                            // 读取超时时间
                .build();
        return this;
    }

    /**
     * @Title: initDefaultArgs
     * @Description: 初始化默认参数
     * @return ApacheHttpClient
     */
    private ApacheHttpClient initDefaultArgs() {
        return this.setDefaultEncoding("UTF-8")
                .setConnectionRequestTimeout(3000)
                .setConnectTimeout(2000)
                .setSocketTimeout(10000);
    }

    /**
     * @Title: buildHttpClientConnectionManager
     * @Description: 构建连接管理器
     * @return ApacheHttpClient this
     * @throws
     */
    public ApacheHttpClient buildHttpClientConnectionManager() {
        // 默认hostname检查策略 ---- 检查 服务器证书subject中是否包含 hostname
//		final HostnameVerifier verifier = new DefaultHostnameVerifier();
        // 空检查器 ---- 不做hostname检查
        final HostnameVerifier verifier = new NoopHostnameVerifier();

        // 自定义注册器，既可以发送http请求，也可以发送https请求
        final Registry<ConnectionSocketFactory> register = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", new SSLConnectionSocketFactory(this.getNoopCheckHostnameSSLContext(), verifier))
                .build();

        PoolingHttpClientConnectionManager poolingMgr = new PoolingHttpClientConnectionManager(register);
        poolingMgr.setMaxTotal(20000);                                // 设置连接池的最大连接数
        poolingMgr.setDefaultMaxPerRoute(poolingMgr.getMaxTotal());    // 一个路由的最大连接数

        return this.setHttpClientConnectionManager(poolingMgr);
    }

    private ApacheHttpClient buildHttpClient() {
        this.client = HttpClients.custom()
                .setRetryHandler(DefaultHttpRequestRetryHandler.INSTANCE)
                .setDefaultRequestConfig(this.requestConfig)
                .setConnectionManagerShared(true)
//				.setSSLSocketFactory(new SSLConnectionSocketFactory(ApacheHttpClient.getSSLContext(), new NoopHostnameVerifier()))
                .setConnectionManager(this.httpClientConnectionManager)
                .build();

        return this;
    }

    /**
     * @Title: getNoopCheckHostnameSSLContext
     * @Description: 获取不校验服务器hostname的sslcontext
     * @return SSLContext
     */
    private SSLContext getNoopCheckHostnameSSLContext() {
        SSLContext ctx = null;
        try {    // 利用apache httpclient 设置 SSL truts检查策略都为true，跳过 服务器证书检查
            ctx = SSLContextBuilder.create()
                    .loadTrustMaterial(null, (X509Certificate[] chain, String authType) -> true)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return ctx;
    }

    public HttpResponse doService(String url, String method, String msg) {
        HttpRequestBase req = null;
        switch (method.trim().toUpperCase()) {
            case "POST":
                req = new HttpPost(url);
                if (msg != null && msg.length() > 0) ((HttpPost) req).setEntity(new StringEntity(msg, "UTF-8"));
                break;
            case "GET":
                req = new HttpGet(url);
                break;
            case "PUT":
                break;
            case "DELETE":
                break;
            default:
                req = new HttpGet(url);
                break;
        }
        return this.doService(req);
    }

    public HttpResponse doService(HttpRequestBase request) {
        HttpResponse resp = null;
        try {
            resp = this.getClient().execute(request);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            this.logger.error("\r\n" + StringUtil.getFullStackTrace(e));
        } catch (IOException e) {
            e.printStackTrace();
            this.logger.error("\r\n" + StringUtil.getFullStackTrace(e));
        }
        return resp;
    }

    public HttpResponse doPost(String url, String msg) {
        return this.doService(url, "POST", msg);
    }

    public HttpResponse doPost(HttpPost request) {
        return this.doService(request);
    }

    public HttpResponse doGet(String url) {
        return this.doService(url, "GET", null);
    }

    public HttpResponse doGet(HttpGet request) {
        return this.doService(request);
    }


    /**
     * @return the httpClientConnectionManager
     */
    public HttpClientConnectionManager getHttpClientConnectionManager() {
        return httpClientConnectionManager;
    }

    /**
     * @param httpClientConnectionManager the httpClientConnectionManager to set
     */
    public ApacheHttpClient setHttpClientConnectionManager(HttpClientConnectionManager httpClientConnectionManager) {
        this.httpClientConnectionManager = httpClientConnectionManager;
        return this;
    }

    /**
     * @return the requestConfig
     */
    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    /**
     * @param requestConfig the requestConfig to set
     */
    public ApacheHttpClient setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
        return this;
    }

    /**
     * @return the defaultEncoding
     */
    public String getDefaultEncoding() {
        return defaultEncoding;
    }

    /**
     * @param defaultEncoding the defaultEncoding to set
     */
    public ApacheHttpClient setDefaultEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
        return this;
    }

    /**
     * @return the connectionRequestTimeout
     */
    public Integer getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    /**
     * @param connectionRequestTimeout the connectionRequestTimeout to set
     */
    public ApacheHttpClient setConnectionRequestTimeout(Integer connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
        return this;
    }

    /**
     * @return the connectTimeout
     */
    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * @param connectTimeout the connectTimeout to set
     */
    public ApacheHttpClient setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * @return the socketTimeout
     */
    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    /**
     * @param socketTimeout the socketTimeout to set
     */
    public ApacheHttpClient setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    /**
     * @return the client
     */
    public CloseableHttpClient getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public ApacheHttpClient setClient(CloseableHttpClient client) {
        this.client = client;
        return this;
    }

    public static void main(String[] args) throws Exception {

        String url = "https://192.168.15.128:8001/einvoice/v1/user/login";
        String msg = "{\"id\":\"uploader\",\"pwd\":\"uploader\"}";

        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(msg, "UTF-8"));
        post.setHeader("Content-Type", "application/json");

        HttpResponse response;

        response = ApacheHttpClient.getInstance().doPost(post);
        System.out.println(EntityUtils.toString(response.getEntity(), Consts.UTF_8));
        for (Header header : response.getAllHeaders())
            System.out.println(header.getName() + " : " + header.getValue());
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("statusCode:" + statusCode);

        url = "https://192.168.15.128:8001/einvoice/v1/user/tokenvalide";
        msg = "{\"token\": \"0qCLEyl6ao6Qjrzyrknj3xIIeixurQWl0HlUj8ftyQ4VqYQYjlwrBcHKJdSgqlCw\"}";

        post = new HttpPost(url);
        post.setEntity(new StringEntity(msg, "UTF-8"));
        post.setHeader("Content-Type", "application/json; charset=utf-8");

        response = ApacheHttpClient.getInstance().doPost(post);
        System.out.println(EntityUtils.toString(response.getEntity(), Consts.UTF_8));
        for (Header header : response.getAllHeaders())
            System.out.println(header.getName() + " : " + header.getValue());


//		response = ApacheHttpClient.getInstance().doGet("http://www.sohu.com/");
//		System.out.println(EntityUtils.toString(response.getEntity(), Consts.UTF_8));
    }
}
