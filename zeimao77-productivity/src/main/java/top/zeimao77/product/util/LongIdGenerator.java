package top.zeimao77.product.util;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.WRONG_SOURCE;

import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.exception.RetryableRuntimeException;

import java.util.concurrent.atomic.AtomicInteger;

public class LongIdGenerator implements IdGenerator<Long>{

    /**
     * 机器号0的实现
     */
    public static final LongIdGenerator INSTANCE = new LongIdGenerator(0x00);
    private AtomicInteger ind = new AtomicInteger(0x00);

    /**
     * 机器ID 范围[0,63]
     */
    private final Long macid;
    private final long timeoffset;

    public LongIdGenerator(final long macid) {
        this(macid,0x018000000000L);
    }

    /**
     * 雪花算法的构造器  41位时间的雪花算法最多支持69.73年
     * 剩余时间可以使用如下公式计算剩余年限：
     * (Math.pow(2,41) - (System.currentTimeMillis() - timeoffset) ) / 3600000 / 24 / 365
     * macid: 机器码占6位 最多支持64台机器
     * ind: 自增码占16位 支持65535个ID自增
     * @param macid 机器ID
     * @param timeoffset 算法的时间偏移
     */
    public LongIdGenerator(final long macid,final long timeoffset) {
        if(macid > 0x3F)
            throw new BaseServiceRunException(WRONG_SOURCE,"机器ID范围从0到63");
        this.macid = macid;
        this.timeoffset = timeoffset;
    }

    /**
     * @return 自增的随机值
     */
    public int getind() {
        if(ind.get() > 0xFFFF) {
            synchronized (this) {
                if(ind.get() > 0xFFFF) {
                    ind.set(0);
                }
            }
        }
        return ind.incrementAndGet();
    }

    /**
     * @return 生成一个ID
     */
    @Override
    public Long generate() {
        long result = 0x0000000000000000L;
        long millis = System.currentTimeMillis() - timeoffset;
        long index = getind();
        result = result | millis << 22;
        result = result | macid << 16;
        result = result | index;
        return result;
    }

    public Long getMacid() {
        return macid;
    }

    public long getTimeoffset() {
        return timeoffset;
    }
}
