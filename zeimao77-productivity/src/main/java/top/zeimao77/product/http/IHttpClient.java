package top.zeimao77.product.http;

import java.util.Map;

public interface IHttpClient {

    /**
     * 可以发送GET请求
     * @param url 请求地址
     * @return 响应结果字符串
     */
    default String sendPost(String url,String body) {
        return sendPost(url,body,null,5);
    }

    String sendPost(String url, String body, Map<String,String> headers, int timeout);

    default String sendGet(String url) {
        return sendGet(url,null,5);
    }

    String sendGet(String url,Map<String,String> headers,int timeout);

}
