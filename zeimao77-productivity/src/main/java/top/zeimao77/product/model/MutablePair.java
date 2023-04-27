package top.zeimao77.product.model;

public class MutablePair<L, R> implements Pair<L, R> {

    private static final long serialVersionUID = 8427267394019331L;
    private L left;
    private R right;

    public MutablePair(){}

    public MutablePair(L left, R right) {
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

    public void setLeft(L left){
        this.left = left;
    }

    public void setRight(R right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MutablePair) {
            MutablePair<?, ?> key = (MutablePair<?, ?>) obj;
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
