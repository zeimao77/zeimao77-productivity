package top.zeimao77.product.model;

import java.util.function.BiPredicate;

/**
 * 三个值的记录类
 */
public class ImmutableRow<L,C,R>implements Row<L, C, R> {

    private static final long serialVersionUID = 6417267394019330L;

    private final L left;
    private final C center;
    private final R right;

    public ImmutableRow(L left,C center,R right) {
        this.left = left;
        this.center = center;
        this.right = right;
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
