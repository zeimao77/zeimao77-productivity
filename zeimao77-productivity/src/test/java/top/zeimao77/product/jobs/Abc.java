package top.zeimao77.product.jobs;

import top.zeimao77.product.util.LongIdGenerator;
import top.zeimao77.product.util.RandomStringUtil;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Random;

public class Abc implements IJob {

    private Long a;
    private Long b;
    private Long c;
    private Long d;
    private String e;
    private String f;
    private String g;
    private String h;
    private String i;
    private String j;
    private String k;
    private String l;
    private String m;
    private String n;
    private LocalDateTime o;
    private LocalDateTime p;
    private BigDecimal q;
    private BigDecimal r;
    private Double s;
    private Double t;
    private ByteBuffer u;
    private ByteBuffer v;
    private ByteBuffer w;
    private ByteBuffer z;

    private Long jobId;

    @Override
    public String jobId() {
        return String.valueOf(jobId);
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public static Abc build() {
        Abc abc = new Abc();
        RandomStringUtil randomString = new RandomStringUtil(0x0F);
        Random r = new Random();
        abc.setA(LongIdGenerator.INSTANCE.generate());
        abc.setB(LongIdGenerator.INSTANCE.generate());
        abc.setC(LongIdGenerator.INSTANCE.generate());
        abc.setD(LongIdGenerator.INSTANCE.generate());
        abc.setE(randomString.randomStr(16));
        abc.setF(randomString.randomStr(16));
        abc.setG(randomString.randomStr(32));
        abc.setH(randomString.randomStr(32));
        abc.setI(randomString.randomStr(64));
        abc.setJ(randomString.randomStr(64));
        abc.setK(randomString.randomStr(128));
        abc.setL(randomString.randomStr(128));
        abc.setM(randomString.randomStr(256));
        abc.setN(randomString.randomStr(256));
        abc.setO(LocalDateTime.now());
        abc.setP(LocalDateTime.now());
        abc.setQ(BigDecimal.ZERO);
        abc.setR(BigDecimal.valueOf(r.nextDouble()));
        abc.setS(r.nextDouble());
        abc.setT(r.nextDouble());
        abc.setU(ByteBuffer.wrap("hello".getBytes(StandardCharsets.UTF_8)));
        abc.setV(ByteBuffer.wrap("world".getBytes(StandardCharsets.UTF_8)));
        abc.setW(ByteBuffer.wrap("hello hello".getBytes(StandardCharsets.UTF_8)));
        abc.setZ(ByteBuffer.wrap("world world".getBytes(StandardCharsets.UTF_8)));
        return abc;
    }

    public Long getA() {
        return a;
    }

    public void setA(Long a) {
        this.a = a;
    }

    public Long getB() {
        return b;
    }

    public void setB(Long b) {
        this.b = b;
    }

    public Long getC() {
        return c;
    }

    public void setC(Long c) {
        this.c = c;
    }

    public Long getD() {
        return d;
    }

    public void setD(Long d) {
        this.d = d;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }

    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getJ() {
        return j;
    }

    public void setJ(String j) {
        this.j = j;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public LocalDateTime getO() {
        return o;
    }

    public void setO(LocalDateTime o) {
        this.o = o;
    }

    public LocalDateTime getP() {
        return p;
    }

    public void setP(LocalDateTime p) {
        this.p = p;
    }

    public BigDecimal getQ() {
        return q;
    }

    public void setQ(BigDecimal q) {
        this.q = q;
    }

    public BigDecimal getR() {
        return r;
    }

    public void setR(BigDecimal r) {
        this.r = r;
    }

    public Double getS() {
        return s;
    }

    public void setS(Double s) {
        this.s = s;
    }

    public Double getT() {
        return t;
    }

    public void setT(Double t) {
        this.t = t;
    }

    public ByteBuffer getU() {
        return u;
    }

    public void setU(ByteBuffer u) {
        this.u = u;
    }

    public ByteBuffer getV() {
        return v;
    }

    public void setV(ByteBuffer v) {
        this.v = v;
    }

    public ByteBuffer getW() {
        return w;
    }

    public void setW(ByteBuffer w) {
        this.w = w;
    }

    public ByteBuffer getZ() {
        return z;
    }

    public void setZ(ByteBuffer z) {
        this.z = z;
    }
}
