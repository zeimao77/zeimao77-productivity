package top.zeimao77.product.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LongIdGeneratorTest extends BaseMain {

    @Test
    @DisplayName("生成LongId")
    public void testGenerate() {
        BaseMain.showBanner();
        logger.info(new LongIdGenerator(0).generate());
        logger.info(new LongIdGenerator(2).generate());
        logger.info(UuidGenerator.INSTANCE.generate());
    }
}