package com.zeimao77;

import com.szjc.FujitsuOrder;
import com.zatca.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.json.Ijson;
import top.zeimao77.product.model.ImmutablePair;
import top.zeimao77.product.model.ImmutableRow;
import top.zeimao77.product.security.DigestUtil;
import top.zeimao77.product.util.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.WRONG_SOURCE;

public class Main {

    public static Logger logger = LoggerFactory.getLogger(Main.class);

    public static final String MYSQLBEAN = "mysql_top_zeimao77";

    public static class Demo {
        private Integer rowNo;
        private BigDecimal qty;
        private Boolean weighGood;

        public Demo(Integer rowNo, BigDecimal qty, Boolean weighGood) {
            this.rowNo = rowNo;
            this.qty = qty;
            this.weighGood = weighGood;
        }

        public Integer getRowNo() {
            return rowNo;
        }

        public void setRowNo(Integer rowNo) {
            this.rowNo = rowNo;
        }

        public BigDecimal getQty() {
            return qty;
        }

        public void setQty(BigDecimal qty) {
            this.qty = qty;
        }

        public Boolean getWeighGood() {
            return weighGood;
        }

        public void setWeighGood(Boolean weighGood) {
            this.weighGood = weighGood;
        }
    }


    public static void main(String[] args) throws NoSuchFieldException {
        Demo demo = new Demo(1, new BigDecimal("1.0"), true);
        System.out.println(BeanUtil.getProperty(demo, "rowNo"));
        System.out.println(BeanUtil.getPropertyType(demo, "qty"));
    }

    public static String removeLeadingZeros(String str) {
        if(str == null)
            return null;
        int i = 0;
        while(i < str.length() && str.charAt(i) == '0') {
            i++;
        }
        return str.substring(i);
    }

    public static void appElement(Document document, Element element,String name, String value) {
        Element element1 = document.createElement(name);
        if(value != null) {
            element1.setTextContent(value);
        }
        element.appendChild(element1);
    }

    public static void xmldoc()  {
        FujitsuOrder fujitsuOrder = new FujitsuOrder();
        fujitsuOrder.setWis_shop_id("1223");
        fujitsuOrder.setWis_busi_date("2025-04-14");
        fujitsuOrder.setWis_shop_type("01");
        FujitsuOrder.Bill bill1 = new FujitsuOrder.Bill();
        bill1.setWis_pos_id("1234567");
        fujitsuOrder.setBill(bill1);
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element sales = document.createElement("SALES");
            sales.setAttribute("xmlns","http://www.baosight.com/shcema/general");
            sales.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
            document.appendChild(sales);
            appElement(document,sales,"WIS_BUSI_DATE",fujitsuOrder.getWis_busi_date());
            appElement(document,sales,"WIS_SHOP_ID",fujitsuOrder.getWis_shop_id());
            appElement(document,sales,"WIS_SHOP_TYPE",fujitsuOrder.getWis_shop_type());
            Element bill = document.createElement("BILL");
            sales.appendChild(bill);
            appElement(document,bill,"WIS_POS_ID",fujitsuOrder.getBill().getWis_pos_id());
            appElement(document,bill,"WIS_BILL_NO",fujitsuOrder.getBill().getWis_bill_no());
            appElement(document,bill,"WIS_BILL_CAT",fujitsuOrder.getBill().getWis_bill_cat());
            appElement(document,bill,"WIS_WORK_SEQ",fujitsuOrder.getBill().getWis_work_seq());
            appElement(document,bill,"WIS_TRAN_SEQ",fujitsuOrder.getBill().getWis_tran_seq());
            appElement(document,bill,"WIS_SALE_TIME",fujitsuOrder.getBill().getWis_sale_time());
            appElement(document,bill,"WIS_BILL_OLDNO",fujitsuOrder.getBill().getWis_bill_oldno());
            appElement(document,bill,"WIS_PERSON_NUM",fujitsuOrder.getBill().getWis_person_num().toString());
            appElement(document,bill,"WIS_ROW_COUNT",fujitsuOrder.getBill().getWis_row_count().toString());
            appElement(document,bill,"WIS_QTY_TOTAL",fujitsuOrder.getBill().getWis_qty_total().toString());
            appElement(document,bill,"WIS_SUM_TOTAL",fujitsuOrder.getBill().getWis_sum_total().toString());
            appElement(document,bill,"WIS_ITEM_DISC",fujitsuOrder.getBill().getWis_item_disc().toString());
            appElement(document,bill,"WIS_BILL_DISC",fujitsuOrder.getBill().getWis_bill_disc().toString());
            appElement(document,bill,"WIS_REAL_TOTAL",fujitsuOrder.getBill().getWis_real_total().toString());
            appElement(document,bill,"WIS_SERVICE_AMT",fujitsuOrder.getBill().getWis_service_amt().toString());
            appElement(document,bill,"WIS_PAY_TOTAL",fujitsuOrder.getBill().getWis_pay_total().toString());
            appElement(document,bill,"WIS_CUT_PAY",fujitsuOrder.getBill().getWis_cut_pay().toString());
            appElement(document,bill,"WIS_OVER_PAY",fujitsuOrder.getBill().getWis_over_pay().toString());
            appElement(document,bill,"WIS_VIP_NO",fujitsuOrder.getBill().getWis_vip_no());
            appElement(document,bill,"WIS_CXXSSUM",fujitsuOrder.getBill().getWis_cxxssum().toString());
            appElement(document,bill,"WIS_CXSELLSUM",fujitsuOrder.getBill().getWis_cxsellsum().toString());
            appElement(document,bill,"WIS_CXYSJE",fujitsuOrder.getBill().getWis_cxysje().toString());
            appElement(document,bill,"WIS_DISC_RATE",fujitsuOrder.getBill().getWis_disc_rate().toString());
            appElement(document,bill,"WIS_OPERATOR",fujitsuOrder.getBill().getWis_operator());
            appElement(document,bill,"WIS_CARRY_OUT",fujitsuOrder.getBill().getWis_carry_out());

            Element details = document.createElement("DETAILS");
            bill.appendChild(details);
            Element detail = document.createElement("DETAIL");
            details.appendChild(detail);
            List<FujitsuOrder.Detail> detailList = fujitsuOrder.getBill().getDetails();
            for (FujitsuOrder.Detail d : detailList) {
                appElement(document,detail,"WISD_BILL_SEQ",d.getWisd_bill_seq().toString());
                appElement(document,detail,"WISD_ITEM_NO",d.getWisd_item_no());
                appElement(document,detail,"WISD_ITEM_NAME",d.getWisd_item_name());
                appElement(document,detail,"WISD_ITEM_CATEGORY",d.getWisd_item_category());
                appElement(document,detail,"WISD_ITEM_SHUILV",d.getWisd_item_shuilv().toString());
                appElement(document,detail,"WISD_ITEM_SPEC",d.getWisd_item_spec());
                appElement(document,detail,"WISD_ITEM_BRAND",d.getWisd_item_brand());
                appElement(document,detail,"WISD_ITEM_UNIT",d.getWisd_item_unit());
                appElement(document,detail,"WISD_ITEM_TYPE",d.getWisd_item_type());
                appElement(document,detail,"WISD_UNIT_PRICE",d.getWisd_unit_price().toString());
                appElement(document,detail,"WISD_RETURN_QTY",d.getWisd_return_qty().toString());
                appElement(document,detail,"WISD_SALES_QTY",d.getWisd_sales_qty().toString());
                appElement(document,detail,"WISD_SALES_AMT",d.getWisd_sales_amt().toString());
                appElement(document,detail,"WISD_DISC_RATE",d.getWisd_disc_rate().toString());
                appElement(document,detail,"WISD_ITEM_DISC",d.getWisd_item_disc().toString());
                appElement(document,detail,"WISD_BILL_DISC",d.getWisd_bill_disc().toString());
                appElement(document,detail,"WISD_REAL_AMT",d.getWisd_real_amt().toString());
                appElement(document,detail,"WISD_ZDCXXUHAO",d.getWisd_zdcxxuhao().toString());
                appElement(document,detail,"WISD_DPCXXUHAO",d.getWisd_dpcxxuhao().toString());
            }
            Element pays = document.createElement("PAYS");
            bill.appendChild(pays);
            Element pay = document.createElement("PAY");
            pays.appendChild(pay);
            List<FujitsuOrder.Pay> payList = fujitsuOrder.getBill().getPays();
            for (FujitsuOrder.Pay p : payList) {
                appElement(document,pay,"WISP_BILL_SEQ",p.getWisp_bill_seq().toString());
                appElement(document,pay,"WISP_PAY_TYPE",p.getWisp_pay_type());
                appElement(document,pay,"WISP_PAY_NAME",p.getWisp_pay_name());
                appElement(document,pay,"WISP_PAY_CURRID",p.getWisp_pay_currid());
                appElement(document,pay,"WISP_PAY_NUMS",p.getWisp_pay_nums().toString());
                appElement(document,pay,"WISP_AS_PAY",p.getWisp_as_pay().toString());
                appElement(document,pay,"WISP_PAY_BASEAMT",p.getWisp_pay_baseamt().toString());
                appElement(document,pay,"WISP_PAY_CARDTYPE",p.getWisp_pay_cardtype());
            }
            TransformerFactory f = TransformerFactory.newInstance();
            Transformer transformer = f.newTransformer();
            DOMSource domSource = new DOMSource(document);
            transformer.transform(domSource,new StreamResult(System.out));
        }catch (Exception e) {

        }

    }

    public static void test1() {

    }

    public static void zatca() throws Exception {

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

    public static void test() {

        ArrayList<OrdersDetailModel> ordersDetailModelList = new ArrayList<>();
        ordersDetailModelList.add(new OrdersDetailModel(2,"0001","000100001",BigDecimal.valueOf(2D)));
        ordersDetailModelList.add(new OrdersDetailModel(3,"0001","000100001",BigDecimal.valueOf(4D)));
        ordersDetailModelList.add(new OrdersDetailModel(4,"0001","000100001",BigDecimal.ONE));
        ordersDetailModelList.add(new OrdersDetailModel(5,"0001","000100001",BigDecimal.ONE));


        HashMap<OrdersDetailModel,BigDecimal> allowRetrurnQty = new HashMap<>();
        for (OrdersDetailModel ordersDetailModel : ordersDetailModelList) {
            allowRetrurnQty.put(ordersDetailModel, ordersDetailModel.getAllowReturnCopies());
        }

        Function<ImmutableRow<String,String, BigDecimal>,OrdersDetailModel> fun = (o) ->{
            OrdersDetailModel secondResult = null;
            for (OrdersDetailModel ordersDetailModel : ordersDetailModelList) {
                if(o.getLeft().equals(ordersDetailModel.getGoodsCode())
                        && o.getCenter().equals(ordersDetailModel.getBarNo())) {
                    BigDecimal allowReturnCopies = allowRetrurnQty.get(ordersDetailModel);
                    if(allowReturnCopies.compareTo(o.getRight()) == 0) {
                        allowRetrurnQty.put(ordersDetailModel, BigDecimal.ZERO);  // 可退数量置零
                        return ordersDetailModel;
                    } else if(allowReturnCopies.compareTo(o.getRight()) > 0) {
                        if(secondResult == null || allowRetrurnQty.get(secondResult).compareTo(allowReturnCopies) > 0) {
                            secondResult = ordersDetailModel;
                        }
                    }
                }
            }
            if (secondResult == null) return null;
            BigDecimal arq = allowRetrurnQty.get(secondResult);
            allowRetrurnQty.put(secondResult, arq.subtract(o.getRight()));
            return secondResult;
        };

        ArrayList<OrdersDetailModel> returnDetailList = new ArrayList<>();
        returnDetailList.add(new OrdersDetailModel(1,"0001","000100001",BigDecimal.valueOf(4D)));
        returnDetailList.add(new OrdersDetailModel(3,"0001","000100001",BigDecimal.valueOf(2D)));
        returnDetailList.add(new OrdersDetailModel(5,"0001","000100001",BigDecimal.valueOf(1D)));
        returnDetailList.add(new OrdersDetailModel(6,"0001","000100001",BigDecimal.valueOf(1D)));
        for (OrdersDetailModel ordersDetailModel : returnDetailList) {
            OrdersDetailModel apply = fun.apply(new ImmutableRow<>(ordersDetailModel.getGoodsCode(), ordersDetailModel.getBarNo(),ordersDetailModel.getAllowReturnCopies()));
            if(apply == null) {
                System.out.println(String.format("退单商品(%s)在原单找不到退可商品行",ordersDetailModel.getGoodsCode()));
                continue;
            }
            System.out.println(String.format("退单商品(%s)找到可退商品行:%d",ordersDetailModel.getGoodsCode(),apply.getRowNo()));
        }

    }

    public static class OrdersDetailModel {
        private Integer rowNo;
        private String goodsCode;
        private String barNo;
        private BigDecimal allowReturnCopies;

        public OrdersDetailModel(Integer rowNo,String goodsCode, String barNo, BigDecimal allowReturnCopies) {
            this.rowNo = rowNo;
            this.goodsCode = goodsCode;
            this.barNo = barNo;
            this.allowReturnCopies = allowReturnCopies;
        }

        public Integer getRowNo() {
            return rowNo;
        }

        public String getGoodsCode() {
            return goodsCode;
        }

        public String getBarNo() {
            return barNo;
        }

        public BigDecimal getAllowReturnCopies() {
            return allowReturnCopies;
        }

    }

}
