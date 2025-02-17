package top.zeimao77.product.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.exception.BaseServiceRunException;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.*;

import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Iterator;
import java.util.Map;


/**
 * http请求工类
 */
public class HttpClientUtil11 implements IHttpClient {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil11.class);

    private HttpClient client;

    public HttpClientUtil11() {
        this(5L);
    }

    public HttpClientUtil11(HttpClient client) {
        this.client = client;
    }

    public HttpClientUtil11(long timeoutSeconds){
        this.client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(timeoutSeconds))
            .build();
    }

    public String sendGet(String url,Map<String,String> headers,int timeout) {
        return sendHttp(this.client,"GET",url,null,headers,timeout,HttpResponse.BodyHandlers.ofString());
    }

    public String sendPost(String url, String body, Map<String, String> headers, int timeout) {
        return sendHttp(this.client,"POST",url,body,headers,timeout,HttpResponse.BodyHandlers.ofString());
    }

    /**
     * @return 客户端对象
     */
    public HttpClient getClient() {
        return client;
    }

    public String sendHttp(String method, String url, String body, Map<String, String> headers, int timeout) {
        return sendHttp(this.client,method,url,body,headers,timeout,HttpResponse.BodyHandlers.ofString());
    }

    public static <T> T sendHttp(HttpClient client,String method, String url, String body, Map<String, String> headers, int timeout, HttpResponse.BodyHandler<T> rbh) {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(timeout))
                .version(HttpClient.Version.HTTP_1_1);
        if(body == null) {
            builder.method(method,HttpRequest.BodyPublishers.noBody());
        } else {
            builder.method(method,HttpRequest.BodyPublishers.ofString(body));
        }
        if(headers != null && !headers.isEmpty()) {
            for(Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();iterator.hasNext();) {
                Map.Entry<String, String> next = iterator.next();
                builder.header(next.getKey(),next.getValue());
            }
        }
        HttpRequest request = builder.build();
        try {
            HttpResponse<T> resp = client.send(request, rbh);
            return resp.body();
        }catch (IOException e) {
            throw new BaseServiceRunException(IOEXCEPTION,"POST请求IO异常:"+e.getMessage(),e);
        } catch (InterruptedException e) {
            logger.error("线程中断错误",e);
            Thread.currentThread().interrupt();
        }
        return null;
    }

}
