package top.zeimao77.product.jobs;

import top.zeimao77.product.util.AssertUtil;

import java.util.concurrent.TimeUnit;

/**
 * 构建示例：
 *     TokenBucket tokenBucket = TokenBucket.Builder.create()
 *         .capacity(120)
 *         .withRefillStrategy(100,1, TimeUnit.SECONDS)
 *         .withSleetStrategy(10,TimeUnit.MILLISECONDS)
 *         .build();
 */
public class TokenBucket {

    // 填充策略
    private RefillStrategy refillStrategy;
    // 睡眠策略
    private SleetStrategy sleetStrategy;
    // 容量
    private long capacity;
    // 剩余令牌数
    private long size;

    /**
     * 尝试获取 立即返回
     * @param numTokens 消费令牌的个数
     * @return 是否成功
     */
    public synchronized boolean tryConsume(long numTokens) {
        AssertUtil.notEmpty( numTokens > 0 && numTokens <= this.capacity, "参数必需大于0且小于桶容器");
        this.refill();
        if (numTokens <= this.size) {
            this.size -= numTokens;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取令牌,睡眠阻塞
     * @param numTokens
     */
    public void consume(long numTokens) {
        while(!this.tryConsume(numTokens)) {
            this.sleetStrategy.sleep();
        }
    }

    private synchronized void refill() {
        long periods = refillStrategy.refill();
        this.size = Math.min(this.capacity,this.size+periods);
    }

    public RefillStrategy getRefillStrategy() {
        return refillStrategy;
    }

    public void setRefillStrategy(RefillStrategy refillStrategy) {
        this.refillStrategy = refillStrategy;
    }

    public SleetStrategy getSleetStrategy() {
        return sleetStrategy;
    }

    public void setSleetStrategy(SleetStrategy sleetStrategy) {
        this.sleetStrategy = sleetStrategy;
    }

    public Long getCapacity() {
        return capacity;
    }

    public static class Builder {
        private long capacity = 1024L;
        private RefillStrategy refillStrategy;
        private SleetStrategy sleetStrategy;

        private Builder(){}

        public static Builder create() {
            Builder builder = new Builder();
            return builder;
        }

        /**
         * 总容量
         * @param capacity
         * @return
         */
        public Builder capacity(long capacity) {
            this.capacity = capacity;
            return this;
        }

        /**
         * 参考方法
         * @see RefillStrategy#RefillStrategy(long, long, java.util.concurrent.TimeUnit)
         */
        public Builder withRefillStrategy(long periodicStep,long period, TimeUnit timeUnit) {
            this.refillStrategy = new RefillStrategy(periodicStep,period,timeUnit);
            return this;
        }

        /**
         * 参考方法
         * @see SleetStrategy#SleetStrategy(long, java.util.concurrent.TimeUnit)
         */
        public Builder withSleetStrategy(long period, TimeUnit timeUnit) {
            this.sleetStrategy = new SleetStrategy(period,timeUnit);
            return this;
        }

        public TokenBucket build(){
            AssertUtil.notEmpty(this.refillStrategy,"刷新策略必需配置");
            TokenBucket tokenBucket = new TokenBucket();
            tokenBucket.refillStrategy = this.refillStrategy;
            tokenBucket.capacity = this.capacity;
            if(this.sleetStrategy != null) {
                tokenBucket.sleetStrategy = this.sleetStrategy;
            } else {
                tokenBucket.sleetStrategy = new SleetStrategy(10,TimeUnit.MILLISECONDS);
            }
            return tokenBucket;
        }
    }

    public static class SleetStrategy {

        private long periodInNanos;

        /**
         * 睡眠策略
         * @param period 时间周期
         * @param timeUnit 时间单位
         */
        private SleetStrategy(long period, TimeUnit timeUnit) {
            this.periodInNanos = timeUnit.toNanos(period);
        }

        private void sleep() {
            try {
                TimeUnit.NANOSECONDS.sleep(periodInNanos);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

    public static class RefillStrategy {
        private long lastRefillTime;
        private long nextRefillTime;
        private long periodInNanos;
        private long periodicStep;

        /**
         * 设置填充策略
         * @param periodicStep 填充步长
         * @param period 时间周期
         * @param timeUnit 时间单位
         */
        public RefillStrategy(long periodicStep,long period, TimeUnit timeUnit) {
            this.lastRefillTime = System.nanoTime();
            this.nextRefillTime = this.lastRefillTime;
            this.periodInNanos = timeUnit.toNanos(period);
            this.periodicStep = periodicStep;
        }

        long refill(){
            long now = System.nanoTime();
            if(now < nextRefillTime) {
                return 0L;
            } else {
                long periods = (now - lastRefillTime) / periodInNanos;
                this.lastRefillTime += periods * this.periodInNanos;
                this.nextRefillTime = this.lastRefillTime + this.periodInNanos;
                return periods * periodicStep;
            }
        }
    }

}
