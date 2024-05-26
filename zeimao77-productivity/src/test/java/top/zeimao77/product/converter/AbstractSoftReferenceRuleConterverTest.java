package top.zeimao77.product.converter;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.UuidGenerator;

import java.time.LocalDateTime;

class AbstractSoftReferenceRuleConterverTest extends BaseMain {

    @Test
    void get() {
        long t = Runtime.getRuntime().totalMemory() / 1024 / 1024;
        logger.info("总内存大小:{}",t);   // 508M
        AbstractSoftReferenceRuleConterver<Object> abstractSoftReferenceRuleConterver = new AbstractSoftReferenceRuleConterver<>() {
            @Override
            protected void refresh() {
                logger.info("刷新规则缓存数据......");
                for (int i = 0; i < 100000; i++) {
                    addConvRule(String.valueOf(i), UuidGenerator.INSTANCE.generate());
                }
            }

            @Override
            protected void refreshExpiryTime() {
                this.expiryTime = LocalDateTime.now().plusSeconds(10);
            }
        };
        abstractSoftReferenceRuleConterver.lock();
        logger.info("{}",abstractSoftReferenceRuleConterver.get("66"));
        abstractSoftReferenceRuleConterver.unLock();
        byte[] oneMegabyte = new byte[490 * 1024 * 1024];
        logger.info("490M空间申请成功");
        oneMegabyte = null;
        abstractSoftReferenceRuleConterver.lock();
        logger.info("{}",abstractSoftReferenceRuleConterver.get("66"));
        abstractSoftReferenceRuleConterver.unLock();
    }
}