package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import static org.junit.jupiter.api.Assertions.*;

class CridUtilTest extends BaseMain {

    @Test
    void removeCheckCode() {
        String id = "11010519990909199X";
        logger.info("计算校验码:{}",CridUtil.calcCheckCode(id));
        logger.info("区域:{}",CridUtil.getRegion(id));
        logger.info("生日:{}",CridUtil.getBirthDay(id));
        logger.info("性别:{}",CridUtil.getSex(id));
        logger.info("Long格式:{}",CridUtil.removeCheckCode(id));
    }

}