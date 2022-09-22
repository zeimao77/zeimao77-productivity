package top.zeimao77.product.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

public class LongIdGeneratorTest extends BaseMain {

    @Test
    @DisplayName("生成LongId")
    public void testGenerate() {
        BaseMain.showBanner();
        LongIdGenerator instance = LongIdGenerator.INSTANCE;
        logger.info(instance.generate());
        // 指定机器号
        logger.info(new LongIdGenerator(2).generate());
        // uuid
        logger.info(UuidGenerator.INSTANCE.generate());
    }
}