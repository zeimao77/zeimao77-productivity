package top.zeimao77.product.jobs;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import java.util.concurrent.TimeUnit;

class TokenBucketTest extends BaseMain {

    @Test
    void consume() {
        TokenBucket tokenBucket = TokenBucket.Builder.create()
            .capacity(120)
            .withRefillStrategy(1,100, TimeUnit.MILLISECONDS)
            .withSleetStrategy(100,TimeUnit.MILLISECONDS)
            .build();
        for (int i = 0; i < 100; i++) {
            tokenBucket.consume(1);
            logger.info("NO:{}",i);
        }

    }
}