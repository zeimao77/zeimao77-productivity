package top.zeimao77.product.converter;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.CollectionUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class AbstractFixedLenConverterTest extends BaseMain {

    public static class TestConverter extends AbstractFixedLenConverter<String> {

        private AbstractNonReFreshConverter<String> store;
        private AtomicLong count = new AtomicLong(0);

        public TestConverter(AbstractNonReFreshConverter<String> store) {
            super(20,10);
            this.store = store;
        }
        @Override
        public Object doGet(String key) {
            count.addAndGet(1L);
            return this.store.get(key);
        }

        public long getCount() {
            return count.get();
        }
    }


    @Test
    public void test() {
        AbstractNonReFreshConverter<String> abstractNonReFreshConverter = new AbstractNonReFreshConverter<>() {
            @Override
            protected void refresh() {
                for (int i = 0; i < 30; i++) {
                    addConvRule(String.format("KEY_%d",i),Integer.valueOf(600+i));
                }
            }
        };
        TestConverter testConverter = new TestConverter(abstractNonReFreshConverter);

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<String> list = List.of(
                "KEY_1","KEY_2","KEY_3","KEY_4","KEY_5","KEY_6","KEY_7","KEY_8"
                ,"KEY_9","KEY_10","KEY_11","KEY_12","KEY_13","KEY_14","KEY_15","KEY_16"
                ,"KEY_17","KEY_18","KEY_19","KEY_20","KEY_21","KEY_22","KEY_23","KEY_24"
        );
        for (int i = 0; i < 10; i++) {
            executorService.submit(()->{
                for (int j = 0; j < 10000; j++) {
                    // delay_ms(2);
                    String random = CollectionUtil.getRandom(list);
                    logger.info("{}:{}",random,testConverter.get(random));
                }
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("命中率: {}/{}",(100000 - testConverter.getCount()),100000);

    }



}
