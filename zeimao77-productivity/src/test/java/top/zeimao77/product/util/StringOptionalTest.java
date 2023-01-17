package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import static org.junit.jupiter.api.Assertions.*;

class StringOptionalTest extends BaseMain {

    @Test
    void ifNotBlack() {
        String param = "123";
        StringOptional stringOptional = new StringOptional(param);
        stringOptional.ifNotBlack(logger::info);
        logger.info("Done..................");
    }

    @Test
    void orBlackGet() {
        String param = " ";
        StringOptional stringOptional = new StringOptional(param);
        String s = stringOptional.orBlackGet(() -> "6666");
        logger.info(s);
        logger.info("Done..................");
    }

    @Test
    void ifBalckThrow() {
        String param = " ";
        StringOptional stringOptional = new StringOptional(param);
        stringOptional.ifBlackThrow("param");
        logger.info("Done..................");
    }

}