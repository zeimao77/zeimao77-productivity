package top.zeimao77.product.dingding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.http.HttpClientUtil11;
import top.zeimao77.product.util.ByteArrayCoDesUtil;
import top.zeimao77.product.util.JsonBeanUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DingDingRobotClient {

    private static Logger logger = LoggerFactory.getLogger(DingDingRobotClient.class);

    private String webhook;
    private String secret;

    public DingDingRobotClient(String webhook, String secret) {
        this.webhook = webhook;
        this.secret = secret;
    }

    public DingDingRobotClient(String webhook,String token, String secret) {
        this.webhook = webhook + "?access_token=" + token;
        this.secret = secret;
    }

    private String sign(long timestamp){
        String stringToSign = timestamp + "\n" + this.secret;
        try {
            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            hmacSHA256.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),"HmacSHA256"));
            byte[] signData = hmacSHA256.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            String sign = URLEncoder.encode(ByteArrayCoDesUtil.base64Encode(signData), StandardCharsets.UTF_8);
            return sign;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void send(String messaage,boolean atAll) {
        sendText(messaage,null,null,atAll);
    }

    private void send(HashMap<String, Object> message,List<String> atMobiles,List<String> atUserIds,boolean atAll) {
        HashMap<String, Object> at = new HashMap<>();
        if(atMobiles != null && !atMobiles.isEmpty())
            at.put("atMobiles",atMobiles);
        if(atUserIds != null && !atUserIds.isEmpty())
            at.put("atUserIds",List.of("zeimao77"));
        at.put("isAtAll",atAll);
        message.put("at",at);
        long timestamp = System.currentTimeMillis();
        String url = this.webhook+"&timestamp="+timestamp;
        url += "&sign="+sign(timestamp);
        Map<String, String> headers = Map.of("Content-Type", "application/json");
        HttpClientUtil11 httpClientUtil11 = new HttpClientUtil11();
        String s = httpClientUtil11.sendPost(url, JsonBeanUtil.DEFAULT.toJsonString(message),headers,5);
        logger.debug("robot:{}",s);
        httpClientUtil11.close();
    }

    public void sendText(String messaage,List<String> atMobiles,List<String> atUserIds,boolean atAll) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("msgtype","text");
        param.put("text",Map.of("content",messaage));
        send(param,atMobiles,atUserIds,atAll);
    }

    public void sendMarkdown(String title,String text,boolean atAll){
        sendMarkdown(title,text,null,null,atAll);
    }

    public void sendMarkdown(String title,String text,List<String> atMobiles,List<String> atUserIds,boolean atAll) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("msgtype","markdown");
        param.put("markdown",Map.of("title",title,"text",text));
        send(param,atMobiles,atUserIds,atAll);
    }


}
