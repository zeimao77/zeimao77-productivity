package top.zeimao77.product.model;

/**
 * 三个值的记录类
 */
public record ImmutableRow<L,C,R>(L left,C center,R right) implements Row<L, C, R> {

    private static final long serialVersionUID = 6417267394019330L;

    @Override
    public L getLeft() {
        return this.left;
    }

    @Override
    public C getCenter() {
        return this.center;
    }

    @Override
    public R getRight() {
        return this.right;
    }
}
