package top.zeimao77.product.http;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.UuidGenerator;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class HttpClientUtilTest extends BaseMain {

    @Test
    void sendGet() {
        HttpClientUtil11 httpClientUtil = new HttpClientUtil11();
        String s = httpClientUtil.sendGet("https://www.baidu.com",null,5);
        logger.info(s);

    }

    @Test
    void testForm() {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        // urlencoded

        String url = "https://www.zeimao77.top/worknote/login";
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/x-www-form-urlencoded");
        Map<String,String> body = new HashMap<>();
        body.put("username","lv");
        body.put("password","123456");
        StringBuilder sBuilder = new StringBuilder();

        for(Iterator<Map.Entry<String, String>> ite = body.entrySet().iterator(); ite.hasNext();) {
            Map.Entry<String, String> next = ite.next();
            if(!sBuilder.isEmpty())
                sBuilder.append("&");
            sBuilder.append(String.format("%s=%s",next.getKey(),next.getValue()));
        }
        HttpClientUtil11.sendHttp(client,"POST", url, sBuilder.toString(), headers, 5, HttpResponse.BodyHandlers.ofString());

        String boundary = "----HttpCliFormBoundary" + UuidGenerator.INSTANCE.generate().substring(20);
        headers.put("Content-Type","multipart/form-data; boundary="+boundary);
        sBuilder = new StringBuilder();
        for(Iterator<Map.Entry<String, String>> ite = body.entrySet().iterator(); ite.hasNext();) {
            Map.Entry<String, String> next = ite.next();
            sBuilder.append(boundary).append("\n");
            sBuilder.append("Content-Disposition:form-data;name=\""+next.getKey()+"\"\n\n");
            sBuilder.append(next.getValue());
            sBuilder.append("\n");
        }
        sBuilder.append(boundary).append("\n");
        logger.info(sBuilder.toString());
        HttpClientUtil11.sendHttp(client,"POST", url, sBuilder.toString(), headers, 5, HttpResponse.BodyHandlers.ofString());
    }

}