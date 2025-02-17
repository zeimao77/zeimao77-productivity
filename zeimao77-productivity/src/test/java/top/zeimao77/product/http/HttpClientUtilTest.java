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
        s = new HttpClientUtil8().sendGet("http://www.zeimao77.top/stadir/66", null, 1000);
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

    @Test
    public void abc() {
        String body = "{\"coupongains\":[],\"couponuses\":[],\"details\":[{\"barcode\":\"6942246202647\",\"brandid\":\"016078\",\"catid\":\"K20501002\",\"channelid\":\"00\",\"discvalue\":0.00,\"entid\":\"3\",\"gdid\":\"327662\",\"gdskuid\":\"327662\",\"gdtype\":\"1\",\"giftflag\":0,\"inputbarcode\":\"6942246202647\",\"isflag\":\"0\",\"mfid\":\"300101\",\"msuid\":\"0\",\"pknumber\":1,\"price\":0.60,\"qty\":1,\"roundvalue\":0.00,\"rowno\":1,\"salevalue\":0.60,\"sheetid\":\"1823933213966368794\",\"shopid\":\"300101\",\"transvalue\":0.60}],\"head\":{\"Changevalue\":0,\"Factpayvalue\":0.60,\"cashierid\":\"0710\",\"changevalue\":0.00,\"channelid\":\"00\",\"custdiscvalue\":0,\"entid\":\"3\",\"factpayvalue\":0.60,\"flag\":0,\"invoiceno\":\"9992021\",\"orderdate\":\"2025-02-13 17:17:35\",\"ordertype\":\"1\",\"payablevalue\":0.60,\"popdiscvalue\":0,\"posbatchno\":\"3\",\"posid\":\"9997\",\"roundvalue\":0.00,\"roundvalue_pay\":0.00,\"senddate\":\"2025-02-13 17:17:35\",\"sendflag\":0,\"sheetid\":\"1823933213966368794\",\"shopid\":\"300101\",\"tempdiscvalue\":0,\"totaldiscvalue\":0,\"totalqty\":1.0,\"totalsalevalue\":0.60},\"payment\":[{\"channelid\":\"00\",\"currencymoney\":0.60,\"entid\":\"3\",\"flag\":1,\"money\":0.60,\"overage\":0.0000,\"paycode\":\"01\",\"payname\":\"人民币\",\"paytype\":\"1\",\"rate\":1,\"rowno\":1,\"sheetid\":\"1823933213966368794\",\"shopid\":\"300101\"}],\"popdetails\":[]}";
        HttpClientUtil11 httpClientUtil11 = new HttpClientUtil11();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json; charset=UTF-8");
        String s = httpClientUtil11.sendHttp( "POST", "http://172.16.248.96/gw/icvs-bmp-som/infOrder/submit?app_id=ipos"
                , body, headers, 5);
        System.out.println(s);
    }


}