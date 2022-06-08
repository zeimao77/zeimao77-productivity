package top.zeimao77.product.model;

import java.util.function.BiPredicate;

public class MutableRow<L,C,R> implements Row<L, C, R> {

    private static final long serialVersionUID = 6417267777719330L;
    private L left;
    private C center;
    private R right;

    public MutableRow(L left,C center,R right) {
        this.left = left;
        this.center = center;
        this.right = right;
    }

    public MutableRow() {}

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
        if(obj instanceof ImmutableRow) {
            ImmutableRow<?,?, ?> key = (ImmutableRow<?,?,?>) obj;
            if(obj == this) return true;
            if(!pre.test(key.getLeft(),this.left)){return false;}
            if(!pre.test(key.getCenter(),this.center)){return false;}
            if(!pre.test(key.getRight(),this.right)){return false;}
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int lhash = this.left == null ? 0 : this.left.hashCode();
        int chash = this.center == null ? 0 : this.center.hashCode();
        int rhash = this.right == null ? 0 : this.right.hashCode();
        return lhash ^ (chash >> 2 | chash << 30) ^ (rhash >> 3 | rhash << 29);
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public void setCenter(C center) {
        this.center = center;
    }

    public void setRight(R right) {
        this.right = right;
    }

    @Override
    public L getLeft() {
        return left;
    }

    @Override
    public C getCenter() {
        return center;
    }

    @Override
    public R getRight() {
        return right;
    }
}
