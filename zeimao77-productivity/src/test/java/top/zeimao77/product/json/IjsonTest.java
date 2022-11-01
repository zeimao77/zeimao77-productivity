package top.zeimao77.product.json;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

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
}