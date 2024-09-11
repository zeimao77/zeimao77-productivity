package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

class StringOptionalTest extends BaseMain {

    @Test
    void ifNotBlank() {
        String param = "123";
        StringOptional stringOptional = new StringOptional(param);
        stringOptional.ifNotBlank(logger::info);
        logger.info("Done..................");
    }

    @Test
    void orBlankGet() {
        String param = " ";
        StringOptional stringOptional = new StringOptional(param);
        String s = stringOptional.orBlankGet(() -> "6666");
        logger.info(s);
        logger.info("Done..................");
    }

    @Test
    void ifBlankThrow() {
        String param = " ";
        StringOptional stringOptional = new StringOptional(param);
        stringOptional.ifBlankThrow("param");
        logger.info("Done..................");
    }

}