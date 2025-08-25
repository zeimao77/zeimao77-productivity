package com.zeimao77;

import com.zatca.*;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import top.zeimao77.product.json.Ijson;
import top.zeimao77.product.security.DigestUtil;
import top.zeimao77.product.util.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class Main {

    public static Logger logger = LoggerFactory.getLogger(Main.class);


    public static final String MYSQLBEAN = "mysql_top_zeimao77";


    public static void main(String[] args) throws Exception{
        // HowSign.testsign();
        // zatca();
        // testBuildZatcaInvoice();
        String s = "EFT:051|TEST01|000003|AMEX      |2503|011457|9853000001     |NTUC TEST TRAINING TLO41 JOO KOON CIRCLE      #13-01 FAIRPRICE HUB   |TESTONLY1234|00|APPROVAL                                |38080750|250821|154913|374245XXXXX1005|0|A63846779537DA8E|APPROVAL                                |null|null|null|38080750|9853000001     ||null|null|null|null|null|null|null|null|null|null";
        SeparatedBySeparator separatedBySeparator = new SeparatedBySeparator(s);
        String value_13 = separatedBySeparator.getValue(13);
        String value_14 = separatedBySeparator.getValue(14);
        System.out.println(value_13);
        String time = "20"+StringUtil.subString(value_13,0,2)
                + "-" + StringUtil.subString(value_13,2,4)
                + "-" +  StringUtil.subString(value_13,4,6)
                + " " + StringUtil.subString(value_14,0,2)
                + ":" + StringUtil.subString(value_14,2,4)
                + ":" + StringUtil.subString(value_14,4,6);
        System.out.println( time);
    }



    public static void zatca() throws Exception {
        // 对XML进行HASH并签名
        // String filePath1 = "E:\\工作文档\\迪拜项目\\SDK\\zatca-einvoicing-sdk-Java-238-R3.3.9\\zatca-einvoicing-sdk-Java-238-R3.3.9\\Data\\Samples\\Standard\\Invoice\\Standard_Invoice.xml";
        // String filePath2 = "E:\\工作文档\\迪拜项目\\SDK\\zatca-einvoicing-sdk-Java-238-R3.3.9\\zatca-einvoicing-sdk-Java-238-R3.3.9\\Data\\Samples\\Simplified\\Invoice\\Simplified_Invoice.xml";
        String filePath2 = "E:\\工作文档\\迪拜项目\\SDK\\zatca-einvoicing-sdk-Java-238-R3.3.9 (2)\\zatca-einvoicing-sdk-Java-238-R3.3.9\\Data\\Samples\\Simplified\\Invoice\\Simplified_Invoice.xml";
        // String filePath2 = "E:\\工作文档\\迪拜项目\\SDK\\zatca-einvoicing-sdk-Java-238-R3.3.9\\zatca-einvoicing-sdk-Java-238-R3.3.9\\Simplified_Invoice.xml";
        filePath2 = "C:\\Users\\zeimao77\\Desktop\\zatca_invoce_test.xml";
        try {
            String s = XMLHasher.generateHash(filePath2);
            System.out.println(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 验签
        byte[] data = ByteArrayCoDesUtil.base64Decode("Hss2gNFjBY5OJn/5CEVZSSNUMrSf4QlCMxwsioPN6fA=");
        byte[] sign = ByteArrayCoDesUtil.base64Decode("MEYCIQCyPQSRiBoMmWKu+SsnHVPfEpd8MFcUh4TM4MOHJGNOZgIhAPN2GDNyJqAEH/eEr0EBGcVuCOmboEENi9LMQEXGXDgb");
        ECDSASignVerifier ecdsaSignVerifier = new ECDSASignVerifier();
        //boolean verify = ecdsaSignVerifier.verify(data, sign);
        // System.out.println("验证结果:"+verify);

    }

    public static void testBuildZatcaInvoice()  {
        ZatcaInvoice zatcaInvoice = new ZatcaInvoice();
        ZatcaInvoice.InvoiceInfo invoiceInfo = new ZatcaInvoice.InvoiceInfo();
        invoiceInfo.setId("SME00010");  // 发票编号?
        String uuid = "e8d654ac-99c0-4909-9940-fb93c03bf69e";
        invoiceInfo.setUuid(uuid);
        invoiceInfo.setIssueDateTime(LocalDateTime.of(2025,8,22,01,41,8));
        invoiceInfo.setInvoiceTypeCode("388");
        zatcaInvoice.setInvoiceInfo(invoiceInfo);

        ZatcaInvoice.AdditionalDocumentReference additionalDocumentReference = new ZatcaInvoice.AdditionalDocumentReference();
        additionalDocumentReference.setId("ICV");
        additionalDocumentReference.setUuid("100");  // 单号
        zatcaInvoice.getAdditionalDocumentReferences().add(additionalDocumentReference);



        zatcaInvoice.setCompanyID(ZatcaConst.VAT_NUMBER);
        ZatcaInvoice.ShopInfo shopInfo = new ZatcaInvoice.ShopInfo();
        shopInfo.setPartyIdentificationId(ZatcaConst.CR);
        shopInfo.setStreetName(ZatcaConst.REGISTERED_ADDRESS);
        shopInfo.setBuildingNumber(ZatcaConst.BUILDING_NUMBER);
        shopInfo.setCitySubdivisionName(ZatcaConst.CITYSUBDIVISIONNAME);
        shopInfo.setCityName(ZatcaConst.CITYNAME);
        shopInfo.setPostalZone(ZatcaConst.POSTAL_ZONE);
        shopInfo.setCountryIdentificationCode(ZatcaConst.COUNTRY_NAME);
        shopInfo.setRegistrationName(ZatcaConst.COMMON_NAME);
        zatcaInvoice.setShopInfo(shopInfo);

        ArrayList<ZatcaInvoice.PaymentMean> paymentMeans = new ArrayList<>();
        paymentMeans.add(new ZatcaInvoice.PaymentMean("10"));
        zatcaInvoice.setPaymentMeans(paymentMeans);

        zatcaInvoice.setInvoiceLines(new ArrayList<>());

        ZatcaInvoice.InvoiceLine invoiceLine = new ZatcaInvoice.InvoiceLine();
        invoiceLine.setId(1);
        invoiceLine.setInvoicedQuantity(new BigDecimal("1.00"));
        invoiceLine.setLineExtensionAmount(new BigDecimal("100.00"));
        invoiceLine.setTaxTotal(new ZatcaInvoice.TaxTotal(new BigDecimal("15.00"), new BigDecimal("115.00")));
        ZatcaInvoice.Price price = new ZatcaInvoice.Price(new BigDecimal("100.00"));
        price.setAllowanceCharge(new ZatcaInvoice.AllowanceCharge(false,new BigDecimal("0.00"),"discount"));
        invoiceLine.setPrice(price);
        ZatcaInvoice.Item item = new ZatcaInvoice.Item("GOOD1");
        item.setClassifiedTaxCategory(new ZatcaInvoice.ClassifiedTaxCategory("S", new BigDecimal("15.00"), "VAT"));
        invoiceLine.setItem(item);
        zatcaInvoice.getInvoiceLines().add(invoiceLine);

        zatcaInvoice.setTaxTotalAmount(new BigDecimal("15.00"));


        ZatcaInvoice.LegalMonetaryTotal legalMonetaryTotal = new ZatcaInvoice.LegalMonetaryTotal();
        legalMonetaryTotal.setLineExtensionAmount(new BigDecimal("100.00"));
        legalMonetaryTotal.setTaxExclusiveAmount(new BigDecimal("100.00"));
        legalMonetaryTotal.setTaxInclusiveAmount(new BigDecimal("115.00"));
        legalMonetaryTotal.setAllowanceTotalAmount(BigDecimal.ZERO);
        legalMonetaryTotal.setPrepaidAmount(BigDecimal.ZERO);
        legalMonetaryTotal.setPayableAmount(new BigDecimal("115.00"));
        zatcaInvoice.setLegalMonetaryTotal(legalMonetaryTotal);

        ZatcaInvoice.TaxTotal taxTotal = new ZatcaInvoice.TaxTotal(new BigDecimal("15.00"),null);
        ZatcaInvoice.TaxSubtotal taxSubtotal = new ZatcaInvoice.TaxSubtotal(new BigDecimal("100.00"), new BigDecimal("15.00"));
        taxSubtotal.setTaxCategory(new ZatcaInvoice.TaxCategory("S", "15.00", "VAT"));
        taxTotal.setTaxSubtotal(taxSubtotal);
        zatcaInvoice.setTaxTotal(taxTotal);

        ZatcaInvoice.InvoiceAuthentication invoiceAuthentication = new ZatcaInvoice.InvoiceAuthentication();
        zatcaInvoice.setInvoiceAuthentication(invoiceAuthentication);

        ZatcaInvoice.AdditionalDocumentReference picRefrence = new ZatcaInvoice.AdditionalDocumentReference();
        picRefrence.setId("PIH");
        picRefrence.setAttachment(new ZatcaInvoice.Attachment(PihValueHolder.INSTANCE.getPihValue()));  // QRCODE
        zatcaInvoice.getAdditionalDocumentReferences().add(picRefrence);

        ZatcaInvoice.AdditionalDocumentReference qrcdoerefrence = new ZatcaInvoice.AdditionalDocumentReference();
        qrcdoerefrence.setId("QR");
        zatcaInvoice.getAdditionalDocumentReferences().add(qrcdoerefrence);

        String binarySecurityToken = "TUlJQ096Q0NBZUNnQXdJQkFnSUdBWmpHc1I2M01Bb0dDQ3FHU000OUJBTUNNQlV4RXpBUkJnTlZCQU1NQ21WSmJuWnZhV05wYm1jd0hoY05NalV3T0RJd01EZzFOVFE0V2hjTk16QXdPREU1TWpFd01EQXdXakJmTVFzd0NRWURWUVFHRXdKVFFURVdNQlFHQTFVRUN3d05VbWw1WVdSb0lFSnlZVzVqYURFVk1CTUdBMVVFQ2d3TVZHaGxJRlpsZHlCTllXeHNNU0V3SHdZRFZRUUREQmhEYjIxd1lXNTVJRk5CUkVZZ1JtOXlJRlJ5WVdScGJtY3dWakFRQmdjcWhrak9QUUlCQmdVcmdRUUFDZ05DQUFSVGtoN3pGaTdIVzZjSUdjdmI0VWx1dXdWMkVtNmxuaTRBMkxncVdmSG5iNzdvZG5wQkxRTVRqU1IzNWFEeENuSjJrdDZCVGlPTzV5QzhHS3VGQ1VXem80SFVNSUhSTUF3R0ExVWRFd0VCL3dRQ01BQXdnY0FHQTFVZEVRU0J1RENCdGFTQnNqQ0JyekU3TURrR0ExVUVCQXd5TVMxVVUxUjhNaTFVVTFSOE15MWxaREl5WmpGa09DMWxObUV5TFRFeE1UZ3RPV0kxT0Mxa09XRTRaakV4WlRRME5XWXhIekFkQmdvSmtpYUprL0lzWkFFQkRBOHpNVEV5TWpjNU9ETXlNREF3TURNeERUQUxCZ05WQkF3TUJEQXhNREF4SkRBaUJnTlZCQm9NR3prMU1qWWdRV3d0VW1GNWVXRmtJREV5TnpNeExUUTROVGdnTnpFYU1CZ0dBMVVFRHd3UlUzVndjR3g1SUdGamRHbDJhWFJwWlhNd0NnWUlLb1pJemowRUF3SURTUUF3UmdJaEFLblZhRUdRM1VNVVJRNzE1RGtENlBHRE44VnlHR1hLS055Uk9pc2tzdnM0QWlFQXFiMGdLOUZ6YmNLK2RzMW4zUFhoOUlDT1BHMEljL0xvbzkwazRJQ3ovMXM9";
        invoiceAuthentication.setBinarySecurityToken(binarySecurityToken);
        BinarySecurityTokenParser.parseCertInfo(null,invoiceAuthentication);
        invoiceAuthentication.setSigningTime(LocalDateTime.now());

        // 创建生成XML小票
        ZatcaInvoceWriter.wirteFile(null,zatcaInvoice,0x00);
        HowSign.sign2(invoiceAuthentication);
        System.out.println(invoiceAuthentication);

        QrCodeDataModel qrCodeDataModel = QrCodeDataModel.buildByZatacInvoice(zatcaInvoice);
        qrcdoerefrence.setAttachment(new ZatcaInvoice.Attachment(qrCodeDataModel.getQrCode()));  // QRCODE

        String filePath = "C:\\Users\\zeimao77\\Desktop\\zatca_invoce_test.xml";
        ZatcaInvoceWriter.wirteFile(filePath,zatcaInvoice,0x0F);
        HashMap<String, String> requestBody = ZatcaInvoceWriter.getRequestBody(zatcaInvoice, 0x0f);
        System.out.println(JsonBeanUtil.DEFAULT.toJsonString(requestBody));
    }



}
