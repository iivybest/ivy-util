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
import org.ivy.util.annotation.Description;
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
 * <p>  classname: ApacheHttpClient
 * <br> description: apache http client
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @date 2019/12/24 20:10
 * @version 1.0
 */
public class ApacheHttpClient {
    // slf4j logger
    private Logger log = LoggerFactory.getLogger(this.getClass());
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
     *  get instance
     * @return ApacheHttpClient
     */
    public static ApacheHttpClient getInstance() {
        return ApacheHttpClientHolder.instance.initialize();
    }


    // ==== step_3 提供工厂方法

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

    @Description({
            "初始化默认参数",
            "构建连接池管理器",
            "构建一般请求参数",
            "构建http client"
    })
    private ApacheHttpClient initialize() {
        this.initDefaultArgs().buildHttpClientConnectionManager()
                .buildRequestConfig().buildHttpClient();
        return this;
    }


    private ApacheHttpClient buildRequestConfig() {
        this.requestConfig = RequestConfig.custom()
                // 从池中获取请求的时间
                .setConnectionRequestTimeout(this.connectionRequestTimeout)
                // 连接超时时间
                .setConnectTimeout(this.connectTimeout)
                // 读取超时时间
                .setSocketTimeout(this.socketTimeout)
                .build();
        return this;
    }

    /**
     * intilized default arguments
     *
     * @return ApacheHttpClient
     */
    private ApacheHttpClient initDefaultArgs() {
        return this.setDefaultEncoding("UTF-8")
                .setConnectionRequestTimeout(3000)
                .setConnectTimeout(2000)
                .setSocketTimeout(10000);
    }

    /**
     * build HttpClientConnectionManager
     *
     * @return ApacheHttpClient
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
        // 设置连接池的最大连接数
        poolingMgr.setMaxTotal(20000);
        // 一个路由的最大连接数
        poolingMgr.setDefaultMaxPerRoute(poolingMgr.getMaxTotal());

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
     *  get NoopCheck Hostname SSLContext
     *  获取不校验服务器hostname的sslcontext
     *
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
            this.log.error("\r\n" + StringUtil.getFullStackTrace(e));
        } catch (IOException e) {
            e.printStackTrace();
            this.log.error("\r\n" + StringUtil.getFullStackTrace(e));
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
     * @return ApacheHttpClient
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
     * @return ApacheHttpClient
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
     * @return ApacheHttpClient
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
     * @return ApacheHttpClient
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
     * @return ApacheHttpClient
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
     * @return ApacheHttpClient
     */
    public ApacheHttpClient setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    /**
     * @return the client
     * @return CloseableHttpClient
     */
    public CloseableHttpClient getClient() {
        return client;
    }

    /**
     * @param client the client to set
     * @return ApacheHttpClient
     */
    public ApacheHttpClient setClient(CloseableHttpClient client) {
        this.client = client;
        return this;
    }

    /**
     * step_2 静态内部类持有外部类的静态对象
     */
    private static class ApacheHttpClientHolder {
        private static ApacheHttpClient instance = new ApacheHttpClient();
    }
}
