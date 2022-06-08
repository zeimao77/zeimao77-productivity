package top.zeimao77.product.dict;

public interface DictEnum<T extends DictEnum> {

    /**
     * @return 字典KEY
     */
    String getKey();

    /**
     * @return 字典值
     */
    String getValue();

}
