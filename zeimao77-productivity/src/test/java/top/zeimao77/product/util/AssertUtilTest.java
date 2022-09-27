package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.main.BaseMain;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.IOEXCEPTION;

public class AssertUtilTest extends BaseMain {


    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(2);
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .executor(executor)
                .build();

        String url = "https://www.baidu.com";
        Map<String,String> headers = new HashMap<>();
        // HttpClient client = HttpClient.newBuilder().build();
        String result = null;
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(5000));
        if(headers != null && !headers.isEmpty()) {
            for(Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry<String, String> next = iterator.next();
                builder.header(next.getKey(),next.getValue());
            }
        }
        HttpRequest request = builder.build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            result = response.body();
        } catch (IOException e) {
            throw new BaseServiceRunException(IOEXCEPTION,"GET请求IO异常",e);
        } catch (InterruptedException e) {
            logger.error("线程中断错误",e);
            Thread.currentThread().interrupt();
        }
        logger.info(result);
        executor.shutdown();

    }

}