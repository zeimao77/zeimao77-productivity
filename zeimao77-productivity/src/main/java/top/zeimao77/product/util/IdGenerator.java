package top.zeimao77.product.util;

/**
 * ID生成接口
 */
public interface IdGenerator<T> {

    /**
     * 生成ID
     * @return ID
     */
    T generate();

}
