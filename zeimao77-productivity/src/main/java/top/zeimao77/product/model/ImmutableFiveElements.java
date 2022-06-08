package top.zeimao77.product.model;

/**
 * @author zeimao77
 * 五个元素的记录类
 * @param <G> 金位元素类型
 * @param <W> 木位元素类型
 * @param <A> 水位元素类型
 * @param <F> 火位元素类型
 * @param <S> 木位元素类型
 */
public record ImmutableFiveElements<G,W,A,F,S>(G gold,W wood,A water,F fair,S soil) implements FiveElements<G,W,A,F,S>{

    @Override
    public G getGold() {
        return this.gold;
    }

    @Override
    public W getWood() {
        return this.wood;
    }

    @Override
    public A getWater() {
        return this.water;
    }

    @Override
    public F getFair() {
        return this.fair;
    }

    @Override
    public S getSoil() {
        return this.soil;
    }

}
