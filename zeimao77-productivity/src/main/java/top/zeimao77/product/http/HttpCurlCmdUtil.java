package top.zeimao77.product.http;

import java.util.Map;

public class HttpCurlCmdUtil implements IHttpClient{

    public static final HttpCurlCmdUtil INSTANCE = new HttpCurlCmdUtil();


    public String sendPost(String url, String body, Map<String, String> headers, int timeout) {
        StringBuilder cmdBuiler = new StringBuilder("curl -X POST");
        if(headers != null && !headers.isEmpty()) {
            for (String s : headers.keySet())
                cmdBuiler.append(" --header '").append(s).append(": ").append(headers.get(s)).append("'");
        }
        if(body != null)
            cmdBuiler.append(" --data '").append(body).append("'");
        if(url != null)
            cmdBuiler.append(" '").append(url).append("'");
        return cmdBuiler.toString();
    }


    public String sendGet(String url, Map<String, String> headers, int timeout) {
        StringBuilder cmdBuiler = new StringBuilder("curl ");
        if(headers != null && !headers.isEmpty()) {
            for (String s : headers.keySet())
                cmdBuiler.append(" --header '").append(s).append(": ").append(headers.get(s)).append("'");
        }
        if(url != null)
            cmdBuiler.append(" '").append(url).append("'");
        return cmdBuiler.toString();
    }

    @Override
    public String sendHttp(String method, String url, String body, Map<String, String> headers, int timeout) {
        return null;
    }
}
