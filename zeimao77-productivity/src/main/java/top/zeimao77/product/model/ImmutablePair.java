package top.zeimao77.product.model;

/**
 * 两个值的记录类
 */
public class ImmutablePair<L, R> implements Pair<L, R> {

    private static final long serialVersionUID = 8417267394019330L;

    private final L left;
    private final R right;

    public ImmutablePair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public L getLeft() {
        return left;
    }

    @Override
    public R getRight() {
        return right;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ImmutablePair) {
            ImmutablePair<?, ?> key = (ImmutablePair<?, ?>) obj;
            if(obj == this) return true;
            if(key.getLeft() != null && key.getLeft().equals(this.left)) {
            } else if(key.getLeft() == null && this.left == null) {
            } else {
                return false;
            }
            if(key.getRight() != null && key.getRight().equals(this.right)) {
            } else if(key.getRight() == null && this.right == null) {
            } else {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int lhash = this.left == null ? 0 : this.left.hashCode();
        int rhash = this.right == null ? 0 : this.right.hashCode();
        return lhash ^ (rhash >> 3 | lhash << 29);
    }

}
