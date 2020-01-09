package org.ivy.xutil.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

public class HttpClient {

    public static byte[] service(String host, String type, Map<String, String> args) {
        byte[] resp = null;

        InputStream in = null;
        PrintWriter out = null;
        ByteArrayOutputStream baos = null;

        URL url = null;
        HttpURLConnection conn = null;

        try {
            url = new URL(host);
            conn = (HttpURLConnection) url.openConnection();

            if (conn == null) {
                return null;
            }
            baos = new ByteArrayOutputStream();

            conn.setRequestMethod(type);
            // 发送POST请求必须设置如下两行
            conn.setDoInput(true);    // 读取connection数据
            conn.setDoOutput(true);   // 写入connection数据


            // 设置及请求的通用属性
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
//	        conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
//			conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Connection", "close");
//			conn.setRequestProperty("Cookie", "dx_un=%E5%A4%A9%E4%B8%8B; dx_avatar=http%3A%2F%2F7xil0e.com1.z0.glb.clouddn.com%2Fuser_580daaa13186d.png; dx_token=6f90b91daeb3a763d38d1a47048cc907; Hm_lvt_d1ede189ce3a27a3412fe7ed21ccbe71=1477290269,1477306349; Hm_lpvt_d1ede189ce3a27a3412fe7ed21ccbe71=1477310729");
            conn.setRequestProperty("Host", "ppt.geekbang.org");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "UTF-8");
            conn.setConnectTimeout(1000 * 60 * 15);
            conn.setReadTimeout(1000 * 60 * 5);
            conn.setUseCaches(true);


            conn.setInstanceFollowRedirects(true);    // 支持server端的redirect
//            conn.setFollowRedirects(true);


            if (args != null) {
                // 获取URLConnection对象对应的输出流
                out = new PrintWriter(conn.getOutputStream());
                // 发送请求参数
                out.print(mapToString(args));
                // flush输出流的缓冲
                out.flush();
            }

            System.out.println("---- status : " + conn.getResponseCode() + " , msg : " + conn.getResponseMessage());

            // 处理链接的 redirect
            boolean redirect = false;
            int status = conn.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK) {
                redirect = status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER;
            }
            if (redirect) {
                // get redirect url from "location" header field
                String newUrl = conn.getHeaderField("Location");
                // get the cookie if need, for login
                String cookies = conn.getHeaderField("Set-Cookie");
                System.out.println("---- Redirect to URL : " + newUrl);

//		        conn = (HttpURLConnection) new URL(newUrl).openConnection();
//		        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//				conn.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
//		        conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
//				conn.setRequestProperty("Connection", "Keep-Alive");
//				conn.setRequestProperty("Cookie", "Hm_lvt_d1ede189ce3a27a3412fe7ed21ccbe71=1477290269,1477306349; Hm_lpvt_d1ede189ce3a27a3412fe7ed21ccbe71=1477360118; dx_un=%E5%A4%A9%E4%B8%8B; dx_avatar=http%3A%2F%2F7xil0e.com1.z0.glb.clouddn.com%2Fuser_580daa84ede3a.png; dx_token=674de707e23be3e96ea247382124f679");
//				conn.setRequestProperty("Host", "pstatic.geekbang.org");
//				conn.setRequestProperty("Referer", "http://ppt.geekbang.org/qconsh2016?amp;isappinstalled=0&amp;amp;amp;amp;from=timeline&amp;amp;amp;amp;isappinstalled=0&amp;amp;amp;from=groupmessage&amp;from=groupmessage&from=groupmessage&isappinstall");
//				conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
//				conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
//				conn.setRequestProperty("charset", "UTF-8");
//				conn.setConnectTimeout(1000 * 60 * 15);

                resp = HttpClient.service(newUrl, "GET", null);
            } else {
                System.out.println("---- conn.getContentLength() : " + conn.getContentLength());

                // 定义BufferedReader输入流来读取URL的响应
                in = conn.getInputStream();
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    baos.write(buf, 0, len);
                }
                resp = baos.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                    out = null;
                }
                if (in != null) {
                    in.close();
                    in = null;
                }
                if (baos != null) {
                    baos.close();
                    baos = null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return resp;
    }

    public static String mapToString(Map<String, String> map) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        if (map != null) {
            for (Entry<String, String> e : map.entrySet()) {
                sb.append(e.getKey())
                        .append("=")
                        .append(URLEncoder.encode(e.getValue(), "UTF-8"))
                        .append("&");
            }
        }
        String str = sb.toString();
        str.substring(0, str.length() - 1);
        return str;
    }


    public static void main(String[] args) {
        System.out.println(new String(HttpClient.service("http://10.120.58.45:8080/hello/", "POST", null)));

    }


}
