package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import static org.junit.jupiter.api.Assertions.*;

class JsonBeanUtilTest extends BaseMain {

    @Test
    void toJsonString() {

        DemoModel demo12345 = new DemoModel(123L, StringOptional.empty());
        logger.info(JsonBeanUtil.DEFAULT.toJsonString(demo12345));
    }

    @Test
    void toBean() {
        String str = "{\"demoId\":1234,\"demoName\":\"demo123\",\"dname\":\"dname1\"}";
        DemoModel demoModel = JsonBeanUtil.DEFAULT.toBean(str, DemoModel.class);
        logger.info("demoname:{}",demoModel.getDemoName());

    }

    private static class DemoModel {
        private Long demoId;
        private StringOptional demoName;
        private String dname;

        public DemoModel() {
        }

        public DemoModel(Long demoId, StringOptional demoName) {
            this.demoId = demoId;
            this.demoName = demoName;
        }

        public Long getDemoId() {
            return demoId;
        }

        public void setDemoId(Long demoId) {
            this.demoId = demoId;
        }

        public StringOptional getDemoName() {
            return demoName;
        }

        public void setDemoName(StringOptional demoName) {
            this.demoName = demoName;
        }

        public String getDname() {
            return dname;
        }

        public void setDname(String dname) {
            this.dname = dname;
        }
    }
}
