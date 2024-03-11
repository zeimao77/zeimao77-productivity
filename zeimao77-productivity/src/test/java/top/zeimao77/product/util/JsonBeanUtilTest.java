package top.zeimao77.product.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import top.zeimao77.product.converter.AbstractNonReFreshConverter;
import top.zeimao77.product.converter.JacksonConvertion;
import top.zeimao77.product.factory.BeanFactory;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.security.JacksonDesensitization;

class JsonBeanUtilTest extends BaseMain {

    @Test
    void toJsonString() {
        BeanFactory.DEFAULT.registerSingleton("con", new AbstractNonReFreshConverter<String>() {
            @Override
            protected void refresh() {
                addConvRule("1","满意");
                addConvRule("2","很满意");
                addConvRule("3","非常满意");
            }
        });
        DemoModel demo12345 = new DemoModel(123L,new StringOptional("张三"),"17788882222");
        logger.info(JsonBeanUtil.DEFAULT.toJsonString(demo12345));
    }

    @Test
    void toBean() {
        String str = "{\"demoId\":1234,\"demoName\":\"demo123\",\"dname\":\"dname1\"}";
        DemoModel demoModel = JsonBeanUtil.DEFAULT.toBean(str, DemoModel.class);
        logger.info("demoname:{}",demoModel.getDemoName());

    }

    @Test
    void testToJsonString() throws JsonProcessingException {
        String j = "{\"b1\":\"YES\"}";
        A a = JsonBeanUtil.DEFAULT.getObjectMapper().readValue(j, A.class);
        logger.info(a.toString());

    }

    public static class A {
        private Boolean b1;

        public A() {
        }

        public A(boolean b1) {
            this.b1 = b1;
        }

        public boolean isB1() {
            return b1;
        }

        public void setB1(boolean b1) {
            this.b1 = b1;
        }
    }


    private static class DemoModel {
        private Long demoId;
        private String mobile;
        private StringOptional demoName;
        @JacksonDesensitization(type = JacksonDesensitization.NAME)
        private String dname;
        @JacksonConvertion(converterBean = "con",format = "full")
        private String ttype;

        public DemoModel() {
        }

        public DemoModel(Long demoId, StringOptional demoName) {
            this.demoId = demoId;
            this.demoName = demoName;
        }


        public DemoModel(Long demoId, StringOptional demoName,String mobile) {
            this.demoId = demoId;
            this.demoName = demoName;
            this.dname = demoName.get();
            this.mobile = mobile;
            this.ttype = "1";
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
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

        public String getTtype() {
            return ttype;
        }

        public void setTtype(String ttype) {
            this.ttype = ttype;
        }
    }
}
