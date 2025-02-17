package top.zeimao77.product.converter;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.model.ImmutablePair;

class AbstractIntervalRefreshConverterTest extends BaseMain {

    @Test
    void getName() {
        AbstractIntervalRefreshConverter<ImmutablePair<Integer, Integer>> converter =
                new AbstractIntervalRefreshConverter<ImmutablePair<Integer, Integer>>(3) {
            @Override
            protected void refresh() {
                clear();
                addConvRule(1, 200, "A1200");
                addConvRule(2, 300, "B2300");
                addConvRule(3, 400, "C3400");

            }

            @Override
            public Object defaultName(ImmutablePair<Integer, Integer> key) {
                return "ERR";
            }

            protected void addConvRule(Integer key1, Integer key2, Object value) {
                // addConvRule(new ImmutablePair<>(key1, key2), value);
            }
        };
        logger.info(converter.getName(new ImmutablePair<>(1,200)));
        delay_ms(3970);
        logger.info(converter.getName(new ImmutablePair<>(1,201)));
    }
}