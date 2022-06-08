package top.zeimao77.product.tree;

/**
 * 投票器接口
 */
public interface Voter<T> {

    int ACCESS_GRANTED = 1;  // 通过
    int ACCESS_ABSTAIN = 0;  // 弃权
    int ACCESS_DENIED = -1;  // 反对

    /**
     * 对一个对象进行投票
     * @param obj 对象
     * @return 投票结果
     */
    int vote(T obj);


}
