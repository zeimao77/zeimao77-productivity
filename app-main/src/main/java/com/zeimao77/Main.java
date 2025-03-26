package com.zeimao77;

import com.zatca.*;
import org.apache.logging.log4j.core.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import sun.nio.cs.ISO_8859_1;
import top.zeimao77.product.fileio.FilesUtil;
import top.zeimao77.product.http.HttpClientUtil11;
import top.zeimao77.product.json.Ijson;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.security.DigestUtil;
import top.zeimao77.product.util.ByteArrayCoDesUtil;
import top.zeimao77.product.util.JsonBeanUtil;
import top.zeimao77.product.util.LocalDateTimeUtil;
import top.zeimao77.product.util.LongBitMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static Logger logger = LoggerFactory.getLogger(Main.class);

    public static final String MYSQLBEAN = "mysql_top_zeimao77";

    public static final String body = """
 {"event":"order.status_update","metadata":{"kafka_message_received_at":"2025-02-26T03:09:18.711214462Z"},"order":{"allergy_note":"NO CUTLERY / 不需要餐具","archived_items":[],"asap":true,"bag_fee":{"currency_code":"HKD","fractional":0},"customer":{"contact_access_code":"803230547","contact_number":"85230189333","first_name":"HK A.","loyalty":{"card_number":""},"order_frequency_at_site":"NEW CUSTOMER"},"customer_order_count":0,"display_id":"2868","id":"hk:81b12954-85b0-4f1f-8b56-025c7acec8c9","items":[{"allowed_actions_if_items_are_unavailable":["ITEM_REMOVAL"],"discount_amount":{"currency_code":"HKD","fractional":0},"id":"d94660d1-4cdd-467e-981a-44ecc49b681d","name":"可口可樂 (單罐裝) 330毫升 Coca Cola (Can) 330ML","operational_name":"可口可樂 330毫升 [G] - 2002947","pos_item_id":"002002947","quantity":1,"total_unit_price":{"currency_code":"HKD","fractional":720},"unit_price":{"currency_code":"HKD","fractional":720},"unreduced_total_unit_price":{"currency_code":"HKD","fractional":720}},{"allowed_actions_if_items_are_unavailable":["ITEM_REMOVAL"],"discount_amount":{"currency_code":"HKD","fractional":300},"id":"9fcd604b-c0b5-405c-850e-addf42a6b599","name":"飛雪礦物質水 1.5升 Mineral Water 1.5L","operational_name":"飛雪礦物質水 1.5升 [G] - 2000016","pos_item_id":"002000016","quantity":2,"total_unit_price":{"currency_code":"HKD","fractional":750},"unit_price":{"currency_code":"HKD","fractional":750},"unreduced_total_unit_price":{"currency_code":"HKD","fractional":750}},{"allowed_actions_if_items_are_unavailable":["ITEM_REMOVAL"],"discount_amount":{"currency_code":"HKD","fractional":165},"id":"c2c325d2-104a-42fb-b9db-2b52c82f8c0a","name":"TOPVALU BP 烏龍茶 525毫升 TOPVALU BP Ulong Tea 525ML","operational_name":"TOPVALU BP 烏龍茶 525毫升 [G] - 20004321","pos_item_id":"020004321","quantity":1,"total_unit_price":{"currency_code":"HKD","fractional":550},"unit_price":{"currency_code":"HKD","fractional":550},"unreduced_total_unit_price":{"currency_code":"HKD","fractional":550}}],"location_id":"1000","offer_discount":{"currency_code":"HKD","fractional":465},"order_type":"delivery","partners_final_order_subtotal":{"currency_code":"HKD","fractional":2305},"partners_final_order_total":{"currency_code":"HKD","fractional":2305},"prepare_for":"2025-02-26T03:07:57Z","promotions":[{"id":"1148a73b-f076-41c6-b927-7b41ba11266c","pos_item_ids":[{"id":"002000016"}],"quantity":0,"type":"percentage_off_on_items","value":20},{"id":"ca43b1b8-c232-41d6-b606-efc614ef5d5e","pos_item_ids":[{"id":"020004321"}],"quantity":0,"type":"percentage_off_on_items","value":30}],"status":"placed","status_log":[{"at":"2025-02-26T03:00:49Z","status":"pending"},{"at":"2025-02-26T03:00:50Z","status":"placed"},{"at":"2025-02-26T03:00:51.206638Z","status":"placed"}],"substituted_items_total":{"currency_code":"HKD","fractional":0},"subtotal_after_substitutions":{"currency_code":"HKD","fractional":2770},"subtotal_price":{"currency_code":"HKD","fractional":2770},"time_to_accept":10,"total_after_substitutions":{"currency_code":"HKD","fractional":2305},"total_price":{"currency_code":"HKD","fractional":2305},"updated_at_epoch_ms":1740538851206}}
            """;

    public static void main(String[] args) throws Exception {

        // 对XML进行HASH并签名
        String filePath1 = "E:\\工作文档\\迪拜项目\\SDK\\zatca-einvoicing-sdk-Java-238-R3.3.9\\zatca-einvoicing-sdk-Java-238-R3.3.9\\Data\\Samples\\Standard\\Invoice\\Standard_Invoice.xml";
        String filePath2 = "E:\\工作文档\\迪拜项目\\SDK\\zatca-einvoicing-sdk-Java-238-R3.3.9\\zatca-einvoicing-sdk-Java-238-R3.3.9\\Data\\Samples\\Simplified\\Invoice\\Simplified_Invoice.xml";

        String base64QrCodeStr = "AQxCb2JzIFJlY29yZHMCDzMxMDEyMjM5MzUwMDAwMwMUMjAyMi0wNC0yNVQxNTozMDowMFoEBzEwMDYuMjUFBjEzMS4yNQYABwAIAAkA";
        try {
            String s = XMLHasher.generateHash(filePath2);
            System.out.println(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 生成二维码
        QrCodeDataModel qrCodeDataModel = new QrCodeDataModel();
        qrCodeDataModel.setSellerName("Bobs Records");
        qrCodeDataModel.setVatNumber("310122393500003");
        qrCodeDataModel.setTimeStamp(LocalDateTime.of(2022, 4, 25, 15, 30, 0));
        qrCodeDataModel.setInvoiceTotal(new BigDecimal("1006.25"));
        qrCodeDataModel.setVatTotal(new BigDecimal("131.25"));
        qrCodeDataModel.getQrCode();
        QrCodeParser qrCodeParser = new QrCodeParser(base64QrCodeStr);
        qrCodeParser.parse();

        // 验签
        byte[] data = ByteArrayCoDesUtil.base64Decode("Hss2gNFjBY5OJn/5CEVZSSNUMrSf4QlCMxwsioPN6fA=");
        byte[] sign = ByteArrayCoDesUtil.base64Decode("MEUCIQCpx+3BuqbNhmFh26OhgYlyZDWrOe2lO2CNMEOSqgtfTQIgS8PexZZNTFNjya3DNfQMU4gphQ+v1mgk7ZHl9TUu5lE=");
        ECDSASignVerifier ecdsaSignVerifier = new ECDSASignVerifier();
        boolean verify = ecdsaSignVerifier.verify(data, sign);
        System.out.println("验证结果:"+verify);



        // 对属性摘要
        String properties = "";
        String result = "NTUzMzVmMjExNWRjYzZkYzRlNjI1Y2Q1NDM1NWMwYjMzZjQ4MTZiYjlhOTZlMmY5ZDkzM2Q3ZDM1ODliNjE0ZA==";
        String s = new String("69a95fc237b42714dc4457a33b94cc452fd9f110504c683c401144d9544894fb");
        System.out.println(ByteArrayCoDesUtil.base64Encode(s.getBytes(StandardCharsets.UTF_8)));

        String hash2 = CertificateHasher.hash(ZatcaConst.CERTIFICATE);
        System.out.println("生成证书HASH2:"+hash2);

        String c = "MIID3jCCA4SgAwIBAgITEQAAOAPF90Ajs/xcXwABAAA4AzAKBggqhkjOPQQDAjBiMRUwEwYKCZImiZPyLGQBGRYFbG9jYWwxEzARBgoJkiaJk/IsZAEZFgNnb3YxFzAVBgoJkiaJk/IsZAEZFgdleHRnYXp0MRswGQYDVQQDExJQUlpFSU5WT0lDRVNDQTQtQ0EwHhcNMjQwMTExMDkxOTMwWhcNMjkwMTA5MDkxOTMwWjB1MQswCQYDVQQGEwJTQTEmMCQGA1UEChMdTWF4aW11bSBTcGVlZCBUZWNoIFN1cHBseSBMVEQxFjAUBgNVBAsTDVJpeWFkaCBCcmFuY2gxJjAkBgNVBAMTHVRTVC04ODY0MzExNDUtMzk5OTk5OTk5OTAwMDAzMFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAEoWCKa0Sa9FIErTOv0uAkC1VIKXxU9nPpx2vlf4yhMejy8c02XJblDq7tPydo8mq0ahOMmNo8gwni7Xt1KT9UeKOCAgcwggIDMIGtBgNVHREEgaUwgaKkgZ8wgZwxOzA5BgNVBAQMMjEtVFNUfDItVFNUfDMtZWQyMmYxZDgtZTZhMi0xMTE4LTliNTgtZDlhOGYxMWU0NDVmMR8wHQYKCZImiZPyLGQBAQwPMzk5OTk5OTk5OTAwMDAzMQ0wCwYDVQQMDAQxMTAwMREwDwYDVQQaDAhSUlJEMjkyOTEaMBgGA1UEDwwRU3VwcGx5IGFjdGl2aXRpZXMwHQYDVR0OBBYEFEX+YvmmtnYoDf9BGbKo7ocTKYK1MB8GA1UdIwQYMBaAFJvKqqLtmqwskIFzVvpP2PxT+9NnMHsGCCsGAQUFBwEBBG8wbTBrBggrBgEFBQcwAoZfaHR0cDovL2FpYTQuemF0Y2EuZ292LnNhL0NlcnRFbnJvbGwvUFJaRUludm9pY2VTQ0E0LmV4dGdhenQuZ292LmxvY2FsX1BSWkVJTlZPSUNFU0NBNC1DQSgxKS5jcnQwDgYDVR0PAQH/BAQDAgeAMDwGCSsGAQQBgjcVBwQvMC0GJSsGAQQBgjcVCIGGqB2E0PsShu2dJIfO+xnTwFVmh/qlZYXZhD4CAWQCARIwHQYDVR0lBBYwFAYIKwYBBQUHAwMGCCsGAQUFBwMCMCcGCSsGAQQBgjcVCgQaMBgwCgYIKwYBBQUHAwMwCgYIKwYBBQUHAwIwCgYIKoZIzj0EAwIDSAAwRQIhALE/ichmnWXCUKUbca3yci8oqwaLvFdHVjQrveI9uqAbAiA9hC4M8jgMBADPSzmd2uiPJA6gKR3LE03U75eqbC/rXA==";
        byte[] sha256s = DigestUtil.digest(c.getBytes(), ZatcaConst.DIGEST_ALGORITHM);
        String s1 = ByteArrayCoDesUtil.hexEncode(sha256s);
        System.out.println("gened HASH3:"+ByteArrayCoDesUtil.base64Encode(s1.getBytes()));

        sha256s = DigestUtil.digest(ByteArrayCoDesUtil.base64Decode(c), ZatcaConst.DIGEST_ALGORITHM);
        String s4 = ByteArrayCoDesUtil.hexEncode(sha256s);
        System.out.println("gened HASH4:"+ByteArrayCoDesUtil.base64Encode(s4.getBytes()));
        System.out.println("right HASH5:ZDMwMmI0MTE1NzVjOTU2NTk4YzVlODhhYmI0ODU2NDUyNTU2YTVhYjhhMDFmN2FjYjk1YTA2OWQ0NjY2MjQ4NQ==");

        System.out.println(ByteArrayCoDesUtil.hexEncode(DigestUtil.digest(ZatcaConst.CERTIFICATE,"MD5")));




    }


    public static List<String> getParameter(String paymentCode){
        String name = "payOctopus,0403|payEleccoupons,0500|payPapercoupons,0502|payDircoupons,0503,0504|payEFT,0901,0902,0903|payACS,0306|payZKcoupons,0505|payMZcoupons,0506|payImpowerCode,0310|paySCB,0303|payMahatan,0307|payJFXF,0800|payJFHG,0707|payAEONCoupon,0509|信用卡,0301|payAEONSelectCoupon,0507|payACSTTH,0314|payNECCoupon,0510,0517|payCLP,0518,05199";
        Pattern pattern = Pattern.compile(paymentCode+",([^|]+)");
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            String group = matcher.group(1);
            String[] split = group.split(",");
            List<String> list = Arrays.asList(split);
            System.out.println(list.size());
            return list;
        }
        return new ArrayList<>();
    }
}
