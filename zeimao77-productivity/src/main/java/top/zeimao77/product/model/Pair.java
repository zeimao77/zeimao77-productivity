package top.zeimao77.product.model;

import java.io.Serializable;

public interface Pair<L,R> extends Serializable {

    /**
     * @return 左值
     */
    L getLeft();

    /**
     * @return 右值
     */
    R getRight();

}
