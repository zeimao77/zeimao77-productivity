package top.zeimao77.product.dingding;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.main.BaseMain;

import static org.junit.jupiter.api.Assertions.*;

class DingDingRobotClientTest extends BaseMain {

    @Test
    void sendMarkdown() {
        String text = """
                ### 提示消息  
                
                这是一个错误处理数据的提示消息,共成功处理1200行,失败8行,下面是处理失败的记录:
                
                - 126235773895704577/未知
                - 126235773895704578/未知
                - 126235773895704580
                - 126235773895704583
                - 126235773895704585
                - 126235773895704586
                - 126236264822210561
                - 126236264834793482
                """;
        DingDingRobotClient client = ComponentFactory.initDingDingRobotClient("robot_dingding", null);
        client.sendMarkdown("提示消息",text,true);
    }
}