package top.zeimao77.product.util;

/**
 * 位图操作
 * @author zeimao77
 * @since 2.1.1
 */
public interface LongBitMap {

    /**
     * 匹配位图
     * @param bitmap 位图
     * @param flag 标志
     * @return 是否匹配
     */
    static boolean matches(long bitmap,long flag) {
        return (bitmap & flag) == flag;
    }

    /**
     * 添加一个标志
     * @param bitmap 位图
     * @param flag 标志
     * @return 匹配后的位图
     */
    static Long add(long bitmap,long flag) {
        return bitmap | flag;
    }

    /**
     * 移除一个标志
     * @param bitmap 位图
     * @param flag 标志
     * @return 移除标志后的位图
     */
    static Long remote(long bitmap,long flag) {
        return bitmap & (~flag);
    }

    /**
     * @param bitmap 位图
     * @return 将位图以字符串方式返回
     */
    static String toBinaryString(long bitmap) {
        return Long.toBinaryString(bitmap);
    }

    /**
     * @param bitmap 位图
     * @return 将位置以十六进制方式返回
     */
    static String toHexString(long bitmap) {
        return Long.toHexString(bitmap);
    }


}
