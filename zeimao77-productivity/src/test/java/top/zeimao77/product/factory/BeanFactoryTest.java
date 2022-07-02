package top.zeimao77.product.factory;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.mysql.DemoModel;
import top.zeimao77.product.util.LongIdGenerator;
import top.zeimao77.product.util.RandomStringUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BeanFactoryTest {

    @Test
    void getBean() {
        RandomStringUtil randomStringUtil = new RandomStringUtil(0x0f);
        BeanFactory.DEFAULT.registerPrototypesFactory("test",() -> {
            DemoModel demoModel = new DemoModel();
            demoModel.setDemoId(LongIdGenerator.INSTANCE.generate());
            demoModel.setDemoName(randomStringUtil.randomStr(6));
            demoModel.setCreatedTime(LocalDateTime.now());
            demoModel.setDe(BigDecimal.valueOf(32.65));
            return demoModel;
        });
        DemoModel test0 = BeanFactory.DEFAULT.getBean("test",DemoModel.class);
        BeanFactory.DEFAULT.registerSingleton(test0.getDemoName(),test0);
        DemoModel test1 = BeanFactory.DEFAULT.getBean("test",DemoModel.class);
        BeanFactory.DEFAULT.registerSingleton(test1.getDemoName(),test1);
        DemoModel bean = BeanFactory.DEFAULT.getBean(test0.getDemoName(), DemoModel.class);
    }
}