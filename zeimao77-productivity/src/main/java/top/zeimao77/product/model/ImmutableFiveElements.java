package top.zeimao77.product.model;

import java.util.function.BiPredicate;

/**
 * @author zeimao77
 * 五个元素的记录类
 * @param <G> 金位元素类型
 * @param <W> 木位元素类型
 * @param <A> 水位元素类型
 * @param <F> 火位元素类型
 * @param <S> 木位元素类型
 */
public class ImmutableFiveElements<G,W,A,F,S> implements FiveElements<G,W,A,F,S>{


    private final G gold;
    private final W wood;
    private final A water;
    private final F fair;
    private final S soil;

    public ImmutableFiveElements(G gold, W wood, A water, F fair, S soil) {
        this.gold = gold;
        this.wood = wood;
        this.water = water;
        this.fair = fair;
        this.soil = soil;
    }

    @Override
    public boolean equals(Object obj) {
        BiPredicate<Object,Object> pre = (o1, o2) -> {
            if(o1 != null && o1.equals(o2)) {
            } else if(o1 == null && o2 == null) {
            } else {
                return false;
            }
            return true;
        };
        if(obj instanceof ImmutableFiveElements) {
            ImmutableFiveElements<?, ?, ?, ?, ?> key = (ImmutableFiveElements<?, ?, ?, ?, ?>) obj;
            if(obj == this) return true;
            if(!pre.test(key.getGold(),this.gold)){return false;}
            if(!pre.test(key.getWood(),this.wood)){return false;}
            if(!pre.test(key.getWater(),this.water)){return false;}
            if(!pre.test(key.getFair(),this.fair)){return false;}
            if(!pre.test(key.getSoil(),this.soil)){return false;}
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int ghash = this.gold == null ? 0 : this.gold.hashCode();
        int whash = this.wood == null ? 0 : this.wood.hashCode();
        int ahash = this.water == null ? 0 : this.water.hashCode();
        int fhash = this.fair == null ? 0 : this.fair.hashCode();
        int shash = this.soil == null ? 0 : this.soil.hashCode();
        return ghash ^ (whash >> 3 | whash << 29) ^ (ahash >> 5 | ahash << 27)
                ^ (fhash >> 7 | fhash << 25) ^ (shash >> 9 | shash << 23);
    }

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
