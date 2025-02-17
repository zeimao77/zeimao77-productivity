package com.zeimao77;

import top.zeimao77.product.http.HttpClientUtil11;
import top.zeimao77.product.json.Ijson;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.ByteArrayCoDesUtil;
import top.zeimao77.product.util.JsonBeanUtil;
import top.zeimao77.product.util.LongBitMap;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends BaseMain {

    public static final String MYSQLBEAN = "mysql_top_zeimao77";

    public static void main(String[] args) {
        for (String s : getParameter("payCLP")) {
            System.out.println(s);
        }
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
