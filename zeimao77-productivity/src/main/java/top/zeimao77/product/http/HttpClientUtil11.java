package top.zeimao77.product.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.zeimao77.product.exception.BaseServiceRunException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * http请求工类
 */
public class HttpClientUtil11 implements AutoCloseable,IHttpClient {

    private static Logger logger = LogManager.getLogger(HttpClientUtil11.class);

    private HttpClient client;
    private ExecutorService executor;

    public HttpClientUtil11(){
        this(1);
    }

    public HttpClientUtil11(HttpClient client) {
        this.client = client;
    }

    /**
     * 线程数
     * @param nthreads
     */
    public HttpClientUtil11(int nthreads){
        this.executor = Executors.newFixedThreadPool(nthreads);
        this.client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .executor(this.executor)
            .build();
    }

    /**
     * 可以发送POST请求
     * @param url 地址
     * @param body 请求体 字符串
     * @return 响应结果字符串
     */
    public String sendPost(String url,String body) {
        return sendPost(this.client,url,body,null,5);
    }

    @Override
    public String sendPost(String url,String body,Map<String,String> headers,int timeout) {
        return sendPost(this.client,url,body,headers,timeout);
    }

    @Override
    public String sendGet(String url,Map<String,String> headers,int timeout) {
        return sendGet(this.client,url,headers,timeout);
    }

    /**
     * 如果需要发送很多请求，我们建议由您来提供client，来避免频繁的建立TCP连接
     * 示例：
     *     HttpClient client = HttpClient.newBuilder()
     *         .connectTimeout(Duration.ofSeconds(5))
     *         .executor(Executors.newFixedThreadPool(3))
     *         .build();
     * @param client HttpClient
     * @param url 请求地址
     * @param headers 请求头
     * @param timeout 超时时间，单位秒
     * @return 以后字符串方式结果请求结果
     */
    public static String sendGet(HttpClient client, String url, Map<String,String> headers,int timeout) {
        String result = null;
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(timeout));
        if(headers != null && !headers.isEmpty()) {
            for(Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();iterator.hasNext();) {
                Map.Entry<String, String> next = iterator.next();
                builder.header(next.getKey(),next.getValue());
            }
        }
        HttpRequest request = builder.build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            result = response.body();
        } catch (IOException e) {
            throw new BaseServiceRunException("GET请求IO异常",e);
        } catch (InterruptedException e) {
            logger.error("线程中断错误",e);
            Thread.currentThread().interrupt();
        }
        return result;
    }

    /**
     * 发送POST请求
     * @param client 请求客户端对象
     * @param url 路径
     * @param body 请求体
     * @param headers 请求头
     * @param titmeout 超时时间
     * @return 以字符串方式结果请求结果
     */
    public static String sendPost(HttpClient client,String url,String body,Map<String,String> headers,int titmeout) {
        String result = null;
        HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers
                .ofString(body);
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(titmeout))
                .POST(requestBody);
        if(headers != null && !headers.isEmpty()) {
            for(Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();iterator.hasNext();) {
                Map.Entry<String, String> next = iterator.next();
                builder.header(next.getKey(),next.getValue());
            }
        }
        HttpRequest request = builder.build();
        try {
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
            result = response.body();
        } catch (IOException e) {
            throw new BaseServiceRunException("POST请求IO异常",e);
        } catch (InterruptedException e){
            logger.error("线程中断错误",e);
            Thread.currentThread().interrupt();
        }

        return result;
    }

    /**
     * @return 客户端对象
     */
    public HttpClient getClient() {
        return client;
    }

    @Override
    public synchronized void close() {
        if(!this.executor.isShutdown()) {
            this.executor.shutdown();
        }
    }

    /**
     * @return 处理线程池
     */
    public ExecutorService getExecutor() {
        return executor;
    }
}
