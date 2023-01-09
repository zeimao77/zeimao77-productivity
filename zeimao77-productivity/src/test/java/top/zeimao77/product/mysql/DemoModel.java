package top.zeimao77.product.mysql;

import top.zeimao77.product.sql.StatementParameterInfo;
import top.zeimao77.product.util.LongIdGenerator;
import top.zeimao77.product.util.RandomStringUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

public class DemoModel {

    private Long demoId;
    private String demoName;
    @StatementParameterInfo(dbtype = 0x00000002,mode = 1,valSetPre = "to_date(",valSetPost = ",'YYYY-MM-DD')")
    private LocalDateTime createdTime;
    private Integer ch;
    private Boolean bo;
    private BigDecimal de;

    @Override
    public String toString() {
        return "DemoModel{" +
                "demoId=" + demoId +
                ", demoName='" + demoName + '\'' +
                ", createdTime=" + createdTime +
                ", ch=" + ch +
                ", bo=" + bo +
                ", de=" + de +
                '}';
    }

    public Long getDemoId() {
        return demoId;
    }

    public void setDemoId(Long demoId) {
        this.demoId = demoId;
    }

    public String getDemoName() {
        return demoName;
    }

    public void setDemoName(String demoName) {
        this.demoName = demoName;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getCh() {
        return ch;
    }

    public void setCh(Integer ch) {
        this.ch = ch;
    }

    public Boolean getBo() {
        return bo;
    }

    public void setBo(Boolean bo) {
        this.bo = bo;
    }

    public BigDecimal getDe() {
        return de;
    }

    public void setDe(BigDecimal de) {
        this.de = de;
    }

    public static class DemoFactory {

        public static final RandomStringUtil randomStringUtil = new RandomStringUtil(0x03);
        public static final Random random = new Random();

        public static DemoModel create() {
            DemoModel demoModel = new DemoModel();
            demoModel.setDemoId(LongIdGenerator.INSTANCE.generate());
            demoModel.setDemoName("asdf'asdf");
            demoModel.setCh(random.nextInt(127));
            demoModel.setBo(demoModel.getCh() % 2 == 1);
            demoModel.setCreatedTime(LocalDateTime.now());
            demoModel.setDe(BigDecimal.valueOf((demoModel.getCh() % 100) / 3.0D));
            return demoModel;
        }

    }

}
