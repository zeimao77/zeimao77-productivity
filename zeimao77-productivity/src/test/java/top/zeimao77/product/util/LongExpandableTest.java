package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

class LongExpandableTest extends BaseMain {

    @Test
    void toBinaryString() {
        for (int i = 0; i < 10; i++) {
            logger.info(UuidGenerator.INSTANCE.generate());
        }
        long extendft0 = 0;
        extendft0 = LongBitMap.add(extendft0,1);
        logger.info("{}",extendft0);
        logger.info("{}", LongBitMap.matches(extendft0,1));
        extendft0 = LongBitMap.remote(extendft0,1);
        logger.info("{}", LongBitMap.matches(extendft0,1));
    }
}