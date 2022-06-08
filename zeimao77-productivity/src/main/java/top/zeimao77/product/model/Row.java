package top.zeimao77.product.model;

import java.io.Serializable;

public interface Row<L,C,R> extends Serializable {

    /**
     * @return 左值
     */
    L getLeft();

    /**
     * @return 中值
     */
    C getCenter();

    /**
     * @return 右值
     */
    R getRight();

}
