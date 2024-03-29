package top.zeimao77.product.model;

import java.util.function.BiPredicate;

public class MutableFiveElements<G,W,A,F,S> implements FiveElements<G,W,A,F,S> {

    private G gold;
    private W wood;
    private A water;
    private F fair;
    private S soil;

    public MutableFiveElements() {
    }

    public MutableFiveElements(G gold, W wood, A water, F fair) {
        this.gold = gold;
        this.wood = wood;
        this.water = water;
        this.fair = fair;
        this.soil = null;
    }

    public MutableFiveElements(G gold, W wood, A water, F fair, S soil) {
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
        if(obj instanceof MutableFiveElements<?,?,?,?,?>) {
            MutableFiveElements<?, ?, ?, ?, ?> key = (MutableFiveElements<?, ?, ?, ?, ?>) obj;
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

    public void setGold(G gold) {
        this.gold = gold;
    }

    public void setWood(W wood) {
        this.wood = wood;
    }

    public void setWater(A water) {
        this.water = water;
    }

    public void setFair(F fair) {
        this.fair = fair;
    }

    public void setSoil(S soil) {
        this.soil = soil;
    }
}
