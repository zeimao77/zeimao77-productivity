package top.zeimao77.product.jobs;

/**
 * 抽象的JOB实现 默认尝试一次
 */
public abstract class AbstractJob implements IJob{

    /**
     * 尝试处理次数 包含第一次尝试
     */
    protected int retry = 1;

    @Override
    public synchronized int consume() {
        return --retry;
    }

}
