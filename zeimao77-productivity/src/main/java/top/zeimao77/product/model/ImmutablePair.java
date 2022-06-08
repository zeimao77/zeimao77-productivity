package top.zeimao77.product.model;

/**
 * 两个值的记录类
 */
public record ImmutablePair<L, R>(L left,R right) implements Pair<L, R> {

    private static final long serialVersionUID = 8417267394019330L;

    @Override
    public L getLeft() {
        return this.left;
    }

    @Override
    public R getRight() {
        return this.right;
    }

}
