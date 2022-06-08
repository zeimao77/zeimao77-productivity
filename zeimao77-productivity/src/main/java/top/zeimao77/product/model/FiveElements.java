package top.zeimao77.product.model;

/**
 * 五个元素的记录接口
 */
public interface FiveElements<G,W,A,F,S> {

    /**
     * @return 金位元素
     */
    G getGold();

    /**
     * @return 木位元素
     */
    W getWood();

    /**
     * @return 水位元素
     */
    A getWater();

    /**
     * @return 火位元素
     */
    F getFair();

    /**
     * @return 土位元素
     */
    S getSoil();

}
