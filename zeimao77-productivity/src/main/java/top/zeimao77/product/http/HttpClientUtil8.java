package top.zeimao77.product.http;

import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.util.StreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class HttpClientUtil8 implements IHttpClient {

    public static final HttpClientUtil8 INSTANCE = new HttpClientUtil8();

    @Override
    public String sendPost(String url, String body, Map<String, String> headers, int timeout) {
        OutputStreamWriter out;
        InputStream is = null;
        String result = null;
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setReadTimeout(timeout * 1000);
            conn.setConnectTimeout(5000);
            if(headers != null) {
                headers.forEach((o1,o2) -> {
                    conn.setRequestProperty(o1,o2);
                });
            }
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out=new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
            out.write(body);
            out.flush();
            out.close();
            is = conn.getInputStream();
            result = StreamUtil.readStream(is);
            conn.getInputStream().close();
        } catch (MalformedURLException e) {
            throw new BaseServiceRunException("URL错误",e);
        } catch (IOException e) {
            throw new BaseServiceRunException("IO错误",e);
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new BaseServiceRunException("IO错误",e);
                }
            }
        }
        return result;
    }

    @Override
    public String sendGet(String url, Map<String, String> headers, int timeout) {
        InputStream is = null;
        String result = null;
        try{
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setReadTimeout(timeout * 1000);
            conn.setConnectTimeout(5000);
            if(headers != null) {
                headers.forEach((o1,o2) -> {
                    conn.setRequestProperty(o1,o2);
                });
            }
            is = conn.getInputStream();
            result = StreamUtil.readStream(is);
        } catch (MalformedURLException e) {
            throw new BaseServiceRunException("URL错误",e);
        } catch (IOException e) {
            throw new BaseServiceRunException("IO错误",e);
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new BaseServiceRunException("IO错误",e);
                }
            }
        }
        return result;
    }

}
