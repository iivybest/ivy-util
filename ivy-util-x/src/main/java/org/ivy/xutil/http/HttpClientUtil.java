package org.ivy.xutil.http;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class HttpClientUtil {
    public static final HttpClientContext context = new HttpClientContext(); // 初始化上下文实例
    private static final HttpClientConnectionManager manager = builderPoolConnectionManager(); // 定义连接池管理变量

    public HttpClientUtil() {
        CookieStore cookieStore = new BasicCookieStore();
        context.setCookieStore(cookieStore);
    }


    /**
     *  getX509TrustManager
     *
     * @return
     * @return X509TrustManager
     * @throws
     */
//	private static X509TrustManager getX509TrustManager() {
//		X509TrustManager mgr = new X509TrustManager() {
//			@Override
//			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//			}
//
//			@Override
//			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//			}
//
//			@Override
//			public X509Certificate[] getAcceptedIssuers() {
//				return null;
//			}
//		};
//		return mgr;
//	}

    /**
     * getSSLContext
     *
     * @return SSLContext
     */
    public static SSLContext getSSLContext() {
        SSLContext ctx = null;

//		ctx = SSLContexts.createDefault();

        try {
            //	ctx = SSLContext.getInstance("TLS");
            //	ctx.init(null, new TrustManager[]{HttpClientUtil.getX509TrustManager()}, null);
            // 利用apache httpclient 设置 SSL truts检查策略都为true，跳过 服务器证书检查
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

    /**
     * @return HttpClientConnectionManager
     */
    public static HttpClientConnectionManager builderPoolConnectionManager() {
        final SSLContext ctx = HttpClientUtil.getSSLContext();

//		final HostnameVerifier verifier = new DefaultHostnameVerifier();
        final HostnameVerifier verifier = new NoopHostnameVerifier();
        // 自定义注册器，既可以发送http请求，也可以发送https请求
        final Registry<ConnectionSocketFactory> register = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(ctx, verifier)).build();

        PoolingHttpClientConnectionManager mgr = new PoolingHttpClientConnectionManager(register);
        mgr.setMaxTotal(200000);                        // 设置连接池的最大连接数
        mgr.setDefaultMaxPerRoute(mgr.getMaxTotal());    // 一个路由的最大连接数
        return mgr;
    }

    public static HttpClient buildHttpClient() {
        // HttpHost proxyHost = new HttpHost("175.155.213.235", 9999);

        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(3000)    // 从池中获取请求的时间
                .setConnectTimeout(2000)            // 连接超时时间
                .setSocketTimeout(5000)            // 读取超时时间
//				.setProxy(proxyHost) 				// 设置代理服务
                .build();

        // 如果使用代理，请打开注释
        // HttpHost proxy = new HttpHost("127.0.0.1" , 2924) ;
        // HttpRoutePlanner httpRoutePlanner = new HttpRoutePlanner() {
        // @Override
        // public HttpRoute determineRoute(HttpHost httpHost, HttpRequest
        // httpRequest, HttpContext httpContext) throws HttpException {
        // return new HttpRoute(httpHost ,proxy);
        // }
        // };

        CloseableHttpClient build = HttpClients.custom()
                .setRetryHandler(DefaultHttpRequestRetryHandler.INSTANCE)
                .setDefaultRequestConfig(config)
//				.setConnectionManagerShared(true)
                .setSSLSocketFactory(new SSLConnectionSocketFactory(HttpClientUtil.getSSLContext(), new NoopHostnameVerifier()))
//				.setSSLSocketFactory(new SSLConnectionSocketFactory(HttpClientUtil.getSSLContext()))
//				.setConnectionManager(manager)
//				.setRoutePlanner(httpRoutePlanner) 设置路由
                .build();

        return build;
    }

    public static void main(String[] args) throws IOException {
        HttpClient httpClient = HttpClientUtil.buildHttpClient();
//		 HttpGet get = new HttpGet("https://www.sina.com.cn");
        HttpGet get = new HttpGet("http://www.sohu.com/");
        HttpResponse response = httpClient.execute(get);
        System.out.println(EntityUtils.toString(response.getEntity(), Consts.UTF_8));

        for (Header header : response.getAllHeaders())
            System.out.println(header.getName() + " : " + header.getValue());
    }

//	public static void main(String[] args) throws Exception {
//
//		String msg = "{\"id\":\"uploader\",\"pwd\":\"uploader\"}";
//
//		HttpClient httpClient = HttpClientUtil.buildHttpClient();
//		HttpPost post = new HttpPost("https://192.168.15.128:8001/einvoice/v1/user/login");
//		post.setEntity(new StringEntity(msg, "UTF-8"));
//
//		HttpResponse response = httpClient.execute(post);
//		System.out.println(EntityUtils.toString(response.getEntity(), Consts.UTF_8));
//
//		for (Header header : response.getAllHeaders())
//			System.out.println(header.getName() + " : " + header.getValue());
//	}

}