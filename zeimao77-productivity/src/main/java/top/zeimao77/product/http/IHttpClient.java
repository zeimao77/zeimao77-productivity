package top.zeimao77.product.http;

import java.util.Map;

public interface IHttpClient {

    String sendGet(String url, Map<String, String> headers, int timeout);

    String sendHttp(String method,String url, String body, Map<String, String> headers, int timeout);

    String sendPost(String url, String body, Map<String, String> headers, int timeout);

}
