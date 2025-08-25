package top.zeimao77.product.math;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class AllocationCalculator<T> {

    private BigDecimal total;

    HashMap<T,BigDecimal> proportion = new HashMap<>();

    public AllocationCalculator(BigDecimal total) {
        this.total = total;
    }

    public void calculate(List<T> sortedList, Function<T,BigDecimal> f) {
        calculate(sortedList,f,2);
    }

    public void calculate(List<T> sortedList, Function<T,BigDecimal> f,int scale) {
        BigDecimal sum = sortedList.stream().map(f).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal remaining = this.total;
        for (int i = 0; i < sortedList.size(); i++) {
            T t = sortedList.get(i);
            if(i == sortedList.size() - 1) {
                proportion.put(t,remaining);
                return;
            }
            BigDecimal apply = f.apply(t);
            BigDecimal p = total.multiply(apply).divide(sum, scale, BigDecimal.ROUND_DOWN);
            proportion.put(t,p);
            remaining = remaining.subtract(p);
        }
    }

    public BigDecimal getAllocation(T t) {
        return proportion.get(t);
    }


}
