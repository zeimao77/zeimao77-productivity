package top.zeimao77.product.json;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.sql.OnlyPrintReposit;
import top.zeimao77.product.util.StreamUtil;

import java.io.PrintWriter;

class IjsonTest extends BaseMain {

    @Test
    void parse() {
        String json = """
                {
                    "employee":[
                        {
                            "id":121212,
                            "name":"John",
                            "phone":"66667777"
                        }, {
                            "name":"tony"
                        }
                    ]
                }
                """;
        Ijson parse = Ijson.parse(json);
        Ijson employee = parse.getJsonArray("employee");
        logger.info("size:{}",employee.size());
        for (int i = 0; i < employee.size(); i++) {
            Ijson jsonObject = employee.getJsonObject(i);
            logger.info(jsonObject.getString("name"));
        }
    }

    @Test
    void foreach() {
        String json = """
                {"shopCode":"081","adef":3.0005,"deliveryDateStart":"2024-05-16 00:00:00","deliveryDateEnd":"2024-05-16 23:59:59"}
                """;
        Ijson parse = Ijson.parse(json);
        OnlyPrintReposit onlyPrintReposit = new OnlyPrintReposit(new PrintWriter(System.out));
        onlyPrintReposit.insertTable("t_delivery_order_query_param",parse, null);
    }
}