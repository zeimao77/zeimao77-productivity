package top.zeimao77.product.dingding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.http.HttpClientUtil11;

public class PushDeerClient {

    private static Logger logger = LoggerFactory.getLogger(PushDeerClient.class);

    private String server = "https://api2.pushdeer.com/message/push";
    private String key;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public PushDeerClient(String key) {
        this.key = key;
    }

    public PushDeerClient() {
    }

    protected void sendText(String messaage) {
        HttpClientUtil11 httpClientUtil11 = new HttpClientUtil11();
        String format = String.format("%s?pushkey=%s&text=%s",this.server,this.key,messaage);
        String s = httpClientUtil11.sendGet(format);
        logger.debug("{}",s);
    }

    public void sendMarkdown(String title,String text) {
        String format = String.format("%s?pushkey=%s&text=%s&desp=%s&type=markdown",this.server,this.key,title,text);
        HttpClientUtil11 httpClientUtil11 = new HttpClientUtil11();
        String s = httpClientUtil11.sendGet(format);
        logger.debug("{}",s);
    }



}
