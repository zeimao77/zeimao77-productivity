package top.zeimao77.product.http;

import java.util.Map;

public interface IHttpClient {

    String sendHttp(String method,String url, String body, Map<String, String> headers, int timeout);

}
