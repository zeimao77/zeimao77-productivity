package top.zeimao77.product.service;

import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.http.HttpClientUtil11;
import top.zeimao77.product.json.Ijson;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.sql.SQL;
import top.zeimao77.product.sql.SimpleSqlClient;
import top.zeimao77.product.util.LocalDateTimeUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.function.BiFunction;

public class InitCalander extends BaseMain {

    private static final String MYSQL="mysql_top_zeimao77";

    public static void main(String[] args) {
        LocalDate localStart = LocalDate.of(2023,10,2);
        SimpleSqlClient simpleSqlClient = ComponentFactory.initSimpleSqlClient(MYSQL,null);

        String urlt = "https://apis.tianapi.com/lunar/index?key=25c7d257ac3b2a94d1ee2004da3994b1&date=%s";
        HttpClientUtil11 httpClientUtil11 = new HttpClientUtil11();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/x-www-form-urlencoded");

        for (int i = 0; i < 70; i++) {
            LocalDate handlerDate = localStart.plusDays(i);
            DayOfWeek dayOfWeek = handlerDate.getDayOfWeek();
            int workingDay = dayOfWeek == DayOfWeek.SUNDAY || dayOfWeek == DayOfWeek.SATURDAY ? 0 : 1;
            String url = String.format(urlt, LocalDateTimeUtil.toDate(handlerDate));
            String s = httpClientUtil11.sendGet(url, headers, 5000);
            logger.info(s);
            Ijson result = Ijson.parse(s).getJsonObject("result");
            String festival = festival(handlerDate, result.getString("lubarmonth"), result.getString("lunarday"));
            SQL sql = new SQL().insert("calendar")
                    .addValues("calendarId", LocalDateTimeUtil.toDate(handlerDate, LocalDateTimeUtil.NUMBERDATEFORMATTER))
                    .addValues("workingDay", workingDay)
                    .addValues("lunarMonth", result.getString("lubarmonth"))
                    .addValues("lunarDay", result.getString("lunarday"))
                    .addValues("festival", festival)
                    .addValues("lunarYear", "贰零贰叁年")
                    .addValues("calendarDay", LocalDateTimeUtil.toDate(handlerDate))
                    .endValues();
            simpleSqlClient.updateByResolver(sql);
            logger.info(sql.getExecSql());
        }

    }


    public static String festival(LocalDate date,String lunarMonth,String lunarDay) {
        String result = null;
        BiFunction<String,String,String> fun = (o1, o2) -> {
            if(o1 != null && o1.length() > 0) {
                return o1 + "," + o2;
            } else {
                return o2;
            }
        };
        if("正月".equals(lunarMonth) && "初一".equals(lunarDay)) {
            result = fun.apply(result,"春节");
        } else if("正月".equals(lunarMonth) && "十五".equals(lunarDay)) {
            result = fun.apply(result,"元霄节");
        } else if("二月".equals(lunarMonth) && "初二".equals(lunarDay)) {
            result = fun.apply(result,"龙头节");
        } else if("五月".equals(lunarMonth) && "初五".equals(lunarDay)) {
            result = fun.apply(result,"端午节");
        } else if("七月".equals(lunarMonth) && "初七".equals(lunarDay)) {
            result = fun.apply(result,"七夕节");
        } else if("七月".equals(lunarMonth) && "十五".equals(lunarDay)) {
            result = fun.apply(result,"中元节");
        } else if("八月".equals(lunarMonth) && "十五".equals(lunarDay)) {
            result = fun.apply(result,"中秋节");
        } else if("九月".equals(lunarMonth) && "初九".equals(lunarDay)) {
            result = fun.apply(result,"重阳节");
        } else if("十月".equals(lunarMonth) && "十五".equals(lunarDay)) {
            result = fun.apply(result,"下元节");
        } else if("腊月".equals(lunarMonth) && "初八".equals(lunarDay)) {
            result = fun.apply(result,"腊八节");
        } else if("腊月".equals(lunarMonth) && "廿三".equals(lunarDay)) {
            result = fun.apply(result,"小年");
        } else if("腊月".equals(lunarMonth) && "三十".equals(lunarDay)) {
            result = fun.apply(result,"除夕");
        }

        if(Month.JANUARY == date.getMonth()) {
            switch (date.getDayOfMonth()) {
                case 1:
                    result = fun.apply(result,"元旦");
                    break;
            }
        }
        if(Month.FEBRUARY == date.getMonth()) {
            switch (date.getDayOfMonth()) {
                case 14:
                    result = fun.apply(result,"情人节");
                    break;
            }
        }
        if(Month.MARCH == date.getMonth()) {
            switch (date.getDayOfMonth()) {
                case 8:
                    result = fun.apply(result,"妇女节");
                    break;
                case 15:
                    result = fun.apply(result,"消费者权益日");
                    break;
            }
        }
        if(Month.APRIL == date.getMonth()) {
            switch (date.getDayOfMonth()) {
                case 1:
                    result = fun.apply(result,"愚人节");
                    break;
            }
        }
        if(Month.MAY == date.getMonth()) {
            switch (date.getDayOfMonth()) {
                case 01:
                    result = fun.apply(result,"劳动节");
                    break;
                case 4:
                    result = fun.apply(result,"青年节");
                    break;
                case 12:
                    result = fun.apply(result,"护士节");
                    break;
                case 31:
                    result = fun.apply(result,"无烟日");
            }
        }
        if(Month.JUNE == date.getMonth()) {
            switch (date.getDayOfMonth()) {
                case 601:
                    result = fun.apply(result,"儿童节");
                    break;
                case 626:
                    result = fun.apply(result,"禁毒日");
            }
        }
        if(Month.JULY == date.getMonth()) {
            switch (date.getDayOfMonth()) {
                case 1:
                    result = fun.apply(result,"建党节");
                    break;
            }
        }
        if(Month.AUGUST == date.getMonth()) {
            switch (date.getDayOfMonth()) {
                case 1:
                    result = fun.apply(result,"建军节");
                    break;
            }
        }
        if(Month.SEPTEMBER == date.getMonth()) {
            switch (date.getDayOfMonth()) {
                case 10:
                    result = fun.apply(result,"教师节");
                    break;
            }
        }
        if(Month.OCTOBER == date.getMonth()) {
            switch (date.getDayOfMonth()) {
                case 1:
                    result = fun.apply(result,"国庆节");
                    break;
            }
        }
        if(Month.NOVEMBER == date.getMonth()) {
            switch (date.getDayOfMonth()) {
                case 8:
                    result = fun.apply(result,"记者节");
                    break;
                case 23:
                    result = fun.apply(result,"感恩节");
                    break;
            }
        }
        if(Month.DECEMBER == date.getMonth()) {
            switch (date.getDayOfMonth()) {
                case 24:
                    result = fun.apply(result,"平安夜");
                    break;
                case 25:
                    result = fun.apply(result,"圣诞节");
                    break;
            }
        }
        return result;
    }

}
