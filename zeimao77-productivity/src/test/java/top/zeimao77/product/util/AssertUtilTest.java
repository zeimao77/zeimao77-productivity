package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.main.BaseMain;

import java.io.Console;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.IOEXCEPTION;

public class AssertUtilTest extends BaseMain {


    public static void main(String[] args) {
        String str0 = "fdmode:3,popzkfd:0.0000,fdresult:1,modifytype:1,end_date:2021-10-07,sta_date:2021-09-24,end_time:23:59,sta_time:00:00";
        String str1 = "fdmode:3,popzkfd:0.8000,fdresult:1,modifytype:1,supzkfd:000002#1.0000|000015#0.9000|000069#0.6000|000079#0.5000|000103#0.6000|000135#0.6000|000366#0.9000|000480#0.5000|000529#0.9000|000649#0.3061|000693#0.5000|000756#0.2693|000830#0.2233|000839#0.4275|000843#0.3275|000848#0.2729|000849#0.2352|000886#0.4000|000887#0.8000|001137#0.5000|001293#0.4111|001308#0.3100|001338#0.4000|001380#0.3000|001420#0.4099|001460#0.5000|001550#0.2654|001600#0.3966|001756#0.1853|001802#0.2540|001871#0.4000|001873#0.3481|001920#0.3421|001956#0.5000|001957#0.5000|001969#0.5000|001987#1.0000|001997#0.5000|002060#0.4792|002088#1.0000|002089#1.0000|002090#0.3751|002116#0.4230|002124#1.0000|002125#0.4000|002135#0.5000|002156#1.0000|002163#0.4186|002164#0.9000|002166#0.4397|002170#1.0000|002215#1.0000|020144#1.0000|020161#0.2756|020162#1.0000|020168#0.3022|020170#0.5000|020180#1.0000|020181#1.0000|020195#0.3100|020222#1.0000|020280#1.0000|020284#1.0000|020286#1.0000|020333#0.5000|020343#0.3319|020356#1.0000|020361#0.4000|020375#0.2000|020379#1.0000|020392#0.8000|020406#1.0000|020410#1.0000|020422#1.0000|020423#1.0000|020426#1.0000|020437#0.5000|020439#1.0000|020443#1.0000|020457#1.0000|020459#1.0000|020464#1.0000|020467#0.6000|020471#1.0000|020474#1.0000|020485#1.0000|020498#0.4000|020499#1.0000|020501#1.0000|020503#1.0000|020506#1.0000|020507#1.0000|020509#1.0000|020519#0.9000|020520#0.9000|020522#0.4000|020531#1.0000|020540#1.0000|020554#0.1500|020585#1.0000|020586#1.0000|020589#0.8000|020609#0.5000|020614#1.0000|020615#1.0000|020621#1.0000|020622#0.8000|020626#1.0000|020633#0.2789|020634#1.0000|020635#1.0000|020639#0.5000|020640#1.0000|020642#1.0000|020643#0.4000|020644#0.9000|020654#0.9000|020663#1.0000|020675#1.0000|020692#1.0000|020695#1.0000|020699#1.0000|020701#1.0000|020703#1.0000|020718#1.0000,end_date:2019-12-12,sta_date:2019-12-07,end_time:23:59,sta_time:00:00";
        String str2 = "fdmode:3,popzkfd:0.5,fdresult:1,modifytype:null,supzkfd:null";
        String str3 = "fdmode:3,popzkfd:0.5,fdresult:1,modifytype:null,supzkfd:";
        Consumer<String> con = str -> {
            String supzkfd = getSupzkfd(str);
            if(supzkfd.equals("")) {
                logger.info("空串");
            } else {
                logger.info(supzkfd);
            }
        };
        con.accept(str0);
        con.accept(str1);
        con.accept(str2);
        con.accept(str3);

    }

    public static String getSupzkfd(String popPolicyMemo){
        if(popPolicyMemo == null)
            return "";
        Pattern pattern = Pattern.compile(".*supzkfd:([^,]+).*");
        Matcher matcher = pattern.matcher(popPolicyMemo);
        if(matcher.matches()) {
            String group = matcher.group(1);
            if("null".equals(group) || "".equals(group)) {
                return "";
            }
            return group;
        }
        return "";
    }
}