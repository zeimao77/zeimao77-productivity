package top.zeimao77.product.tree;

import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.util.AssertUtil;

import java.util.List;

public class ThresholdVoterComponent<T> implements Voter<T> {

    /**
     * 表决器阈值
     * 阈值用于控制表决行为
     * 1D：一票否决
     * 0D：一票通过
     * 0D &gt; threshold &gt; 1D : 如果 投票通过的票数/投票器列表数量 >= threshold 将会被表决通过;
     */
    private Double threshold;

    // 投票器列表
    List<Voter> voters;

    /**
     * 构造一个表决器
     * @param voters  投票器列表
     * @param threshold 阈值
     */
    public ThresholdVoterComponent(List<Voter> voters, Double threshold) {
        AssertUtil.assertTure(threshold >= 0D && threshold <= 1D,"错误的阈值");
        this.voters = voters;
        this.threshold = threshold;
    }

    @Override
    public int vote(T obj) {
        int gc = 0,dc = 0;
        for (Voter voter : voters) {
            int vote = voter.vote(obj);
            switch (vote) {
                case ACCESS_GRANTED:
                    gc++;
                    break;
                case ACCESS_DENIED :
                    dc++;
                    break;
            }
            if(threshold.compareTo(0D) == 0 && gc > 0) {
                return ACCESS_GRANTED;
            } else if(threshold.compareTo(1D) == 0 && dc > 0) {
                return ACCESS_DENIED;
            } else if(threshold.compareTo(0D) == 0 && dc == voters.size()) {
                return ACCESS_DENIED;
            } else if(threshold.compareTo(1D) == 0 && gc == voters.size()) {
                return ACCESS_GRANTED;
            } else if(threshold.compareTo(0D) > 0 && threshold.compareTo(1D) < 0) {
                /**
                 * 并不会将所有投票器执行完毕进行唱票
                 * 只要通过的票数超过了阈值 或者 否决的票数超过了 1 - 阈值 则直接产出结果;
                 */
                if((Double.valueOf(gc)/voters.size()) >= threshold) {
                    return ACCESS_GRANTED;
                }
                if((Double.valueOf(dc)/voters.size()) > (1-threshold)) {
                    return ACCESS_DENIED;
                }
            }
        }
        throw new BaseServiceRunException("表决错误");
    }
}
