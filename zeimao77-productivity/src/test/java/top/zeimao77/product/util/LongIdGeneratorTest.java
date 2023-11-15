package top.zeimao77.product.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

public class LongIdGeneratorTest extends BaseMain {

    @Test
    @DisplayName("生成LongId")
    public void testGenerate() {
<<<<<<< HEAD
        LongIdGenerator instance = LongIdGenerator.INSTANCE;
        logger.info(instance.generate().toString());
        // 指定机器号
        logger.info(new LongIdGenerator(2).generate().toString());
=======
        BaseMain.showBanner("0.0.1");
        LongIdGenerator instance = LongIdGenerator.INSTANCE;
        logger.info("{}",instance.generate());
        // 指定机器号
        logger.info("{}",new LongIdGenerator(2).generate());
>>>>>>> main
        // uuid
        logger.info(UuidGenerator.INSTANCE.generate());
    }
}