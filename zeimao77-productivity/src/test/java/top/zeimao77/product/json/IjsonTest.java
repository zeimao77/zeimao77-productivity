package top.zeimao77.product.json;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import static org.junit.jupiter.api.Assertions.*;

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
                        }
                    ]
                }
                """;
        Ijson parse = Ijson.parse(json);
        String string = parse.getJsonArray("employee").getJsonObject(0).getString("name");
        logger.info(string);
    }
}