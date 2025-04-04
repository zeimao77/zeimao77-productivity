package top.zeimao77.product.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.exception.BaseServiceRunException;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.*;
import top.zeimao77.product.util.StreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class HttpClientUtil8 implements IHttpClient {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil8.class);
    public static final HttpClientUtil8 INSTANCE = new HttpClientUtil8();


    public static void sendHttp(String method, String url, String body, Map<String, String> headers, int timeout, Consumer<InputStream> con) {
        OutputStreamWriter out;
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            conn = (HttpURLConnection)realUrl.openConnection();
            conn.setRequestMethod(method);
            conn.setReadTimeout(timeout * 1000);
            conn.setConnectTimeout(5000);
            if(headers != null && !headers.isEmpty()) {
                for (String o : headers.keySet())
                    conn.setRequestProperty(o,headers.get(o));
            }
            conn.setDoOutput(true);
            conn.setDoInput(true);
            if(body != null) {
                out=new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
                out.write(body);
                out.flush();
                out.close();
            }
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK)
                throw new BaseServiceRunException(APPERR,"HTTP请求错误:"+responseCode);
            is = conn.getInputStream();
            con.accept(is);
            conn.getInputStream().close();
        } catch (MalformedURLException e) {
            throw new BaseServiceRunException(WRONG_SOURCE,"URL错误",e);
        } catch (IOException e) {
            throw new BaseServiceRunException(IOEXCEPTION,"IO错误",e);
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new BaseServiceRunException(IOEXCEPTION,"IO错误:"+e.getMessage(),e);
                }
            }
        }
    }

    @Override
    public String sendHttp(String method,String url, String body, Map<String, String> headers, int timeout) {
        AtomicReference<String> result = null;
        sendHttp(method,url,body,headers,timeout,(is)->{
            result.set(StreamUtil.readStream(is));
        });
        return result.get();
    }

    @Override
    public String sendPost(String url, String body, Map<String, String> headers, int timeout) {
        return sendHttp("POST",url,body,headers,timeout);
    }

    @Override
    public String sendGet(String url, Map<String, String> headers, int timeout) {
       return sendHttp("GET",url,null,headers,timeout);
    }

}
