package com.zeimao77;

import top.zeimao77.product.cmd.ProgressBar;
import top.zeimao77.product.config.LocalContext;
import top.zeimao77.product.jobs.TokenBucket;
import top.zeimao77.product.json.Ijson;
import top.zeimao77.product.main.BaseMain;

import top.zeimao77.product.model.ImmutableRow;
import top.zeimao77.product.sql.OnlyPrintReposit;
import top.zeimao77.product.util.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class Main extends BaseMain {

    private static String json = """
            [
                {
                    "stibarcode": "69021824",
                    "stibatchno": "202404261035117730201nrTjta29002",
                    "sticustomer": "81330200681066485J",
                    "stidate": "2024-04-27 21:30:07",
                    "stigdid": "1082885",
                    "stigrantreason": "02",
                    "stiinvno": 17261,
                    "stimktno": "00185",
                    "stioutinvno": "00018520111714206334481",
                    "stipopdisamt": 48.15,
                    "stiposno": "2011",
                    "stirowno": 0,
                    "stisaleprice": 9.69,
                    "stisaleqnt": 6,
                    "stisupplierno": "Z44269",
                    "stiswapamt": 55.14
                },
                {
                    "stibarcode": "6901347880390",
                    "stibatchno": "202404261035117730201nrTjta29002",
                    "sticustomer": "81330200681066485J",
                    "stidate": "2024-04-27 21:30:07",
                    "stigdid": "1170406",
                    "stigrantreason": "02",
                    "stiinvno": 17261,
                    "stimktno": "00185",
                    "stioutinvno": "00018520111714206334481",
                    "stipopdisamt": 12.05,
                    "stiposno": "2011",
                    "stirowno": 1,
                    "stisaleprice": 14.3,
                    "stisaleqnt": 1,
                    "stisupplierno": "Z44269",
                    "stiswapamt": 13.8
                },
                {
                    "stibarcode": "6933645421777",
                    "stibatchno": "202404261035117730201nrTjta29002",
                    "sticustomer": "81330200681066485J",
                    "stidate": "2024-04-27 21:30:07",
                    "stigdid": "1118953",
                    "stigrantreason": "02",
                    "stiinvno": 17261,
                    "stimktno": "00185",
                    "stioutinvno": "00018520111714206334481",
                    "stipopdisamt": 6.36,
                    "stiposno": "2011",
                    "stirowno": 2,
                    "stisaleprice": 10.4,
                    "stisaleqnt": 1,
                    "stisupplierno": "Z44269",
                    "stiswapamt": 7.28
                },
                {
                    "stibarcode": "6928804011227",
                    "stibatchno": "202404261035117730201nrTjta29002",
                    "sticustomer": "81330200681066485J",
                    "stidate": "2024-04-27 21:30:07",
                    "stigdid": "1160016",
                    "stigrantreason": "02",
                    "stiinvno": 17261,
                    "stimktno": "00185",
                    "stioutinvno": "00018520111714206334481",
                    "stipopdisamt": 5.58,
                    "stiposno": "2011",
                    "stirowno": 3,
                    "stisaleprice": 6.6,
                    "stisaleqnt": 1,
                    "stisupplierno": "Z44269",
                    "stiswapamt": 6.39
                },
                {
                    "stibarcode": "6928804010381",
                    "stibatchno": "202404261035117730201nrTjta29002",
                    "sticustomer": "81330200681066485J",
                    "stidate": "2024-04-27 21:30:07",
                    "stigdid": "1160031",
                    "stigrantreason": "02",
                    "stiinvno": 17261,
                    "stimktno": "00185",
                    "stioutinvno": "00018520111714206334481",
                    "stipopdisamt": 5.49,
                    "stiposno": "2011",
                    "stirowno": 4,
                    "stisaleprice": 6.49,
                    "stisaleqnt": 1,
                    "stisupplierno": "Z44269",
                    "stiswapamt": 6.29
                },
                {
                    "stibarcode": "6942404210040",
                    "stibatchno": "202404261035117730201nrTjta29002",
                    "sticustomer": "81330200681066485J",
                    "stidate": "2024-04-27 21:30:07",
                    "stigdid": "1210057",
                    "stigrantreason": "02",
                    "stiinvno": 17261,
                    "stimktno": "00185",
                    "stioutinvno": "00018520111714206334481",
                    "stipopdisamt": 4.97,
                    "stiposno": "2011",
                    "stirowno": 5,
                    "stisaleprice": 5.99,
                    "stisaleqnt": 1,
                    "stisupplierno": "Z44269",
                    "stiswapamt": 5.69
                },
                {
                    "stibarcode": "6901845040968",
                    "stibatchno": "202404261035117730201nrTjta29002",
                    "sticustomer": "81330200681066485J",
                    "stidate": "2024-04-27 21:30:07",
                    "stigdid": "3110659",
                    "stigrantreason": "02",
                    "stiinvno": 17261,
                    "stimktno": "00185",
                    "stioutinvno": "00018520111714206334481",
                    "stipopdisamt": 6.79,
                    "stiposno": "2011",
                    "stirowno": 6,
                    "stisaleprice": 8.19,
                    "stisaleqnt": 1,
                    "stisupplierno": "Z44269",
                    "stiswapamt": 7.78
                },
                {
                    "stibarcode": "6920907808278",
                    "stibatchno": "202404261035117730201nrTjta29002",
                    "sticustomer": "81330200681066485J",
                    "stidate": "2024-04-27 21:30:07",
                    "stigdid": "1017359",
                    "stigrantreason": "02",
                    "stiinvno": 17261,
                    "stimktno": "00185",
                    "stioutinvno": "00018520111714206334481",
                    "stipopdisamt": 10.61,
                    "stiposno": "2011",
                    "stirowno": 7,
                    "stisaleprice": 4.28,
                    "stisaleqnt": 3,
                    "stisupplierno": "Z44269",
                    "stiswapamt": 12.15
                }
            ]
            """;


    public static void main(String[] args) throws UnsupportedEncodingException {
        String[] split = "abc,444".split(",");
        for (String s : split) {
            System.out.println(s);
        }
    }

    public static void InsertCoupon(Long oid,String shardingCode,Integer rowNo, Double amount) {
        var sqlFormat = "INSERT INTO ordersusecoupon(oucId, oid, shardingCode, entId, rowNo, rowNoId, payCode, payRowNo" +
                ", couponGroup, couponType, couponClass, couponNo, couponCost, amount, originalMount, eventId, policyId" +
                ", khFlag, nsta, lang, receiveDate) VALUES (%d, %d, '%s', 0, %d, null" +
                ", '0110', 1, '02', '1017', NULL, NULL, NULL, %.2f, %.2f, null, null, NULL, NULL, 'CN', '2024-05-06 15:00:00');";
        System.out.printf(sqlFormat,LongIdGenerator.INSTANCE.generate(),oid,shardingCode,rowNo,amount,amount);
    }


    public static void InsertErp() {
        PrintWriter printWriter = StreamUtil.printWriter("C:\\Users\\zeimao77\\Desktop\\00018520111714206334481.sql");
        Ijson parse = Ijson.parse(json);
        for (int i = 0; i < 8; i++) {
            Ijson jsonObject = parse.getJsonObject(i);
            Saleticket saleticket = JsonBeanUtil.DEFAULT.toBean(jsonObject.toJsonString(false), Saleticket.class);
            OnlyPrintReposit onlyPrintReposit = new OnlyPrintReposit(printWriter);
            onlyPrintReposit.insertTable("SHIJI_SALETICKET",saleticket,(o1,o2) -> {
                if("stidate".equalsIgnoreCase(o1)) {
                    return new ImmutableRow<>(true,"to_date (",",'YYYY-MM-DD HH24:MI:SS')");
                }
                return new ImmutableRow<>(true,null,null);
            });
        }
        printWriter.close();
    }

}
