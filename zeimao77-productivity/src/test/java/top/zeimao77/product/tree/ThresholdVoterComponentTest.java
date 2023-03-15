package top.zeimao77.product.tree;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import java.util.Arrays;

public class ThresholdVoterComponentTest extends BaseMain {

    @Test
    public void vote() {
        ThresholdVoterComponent<Integer> component = new ThresholdVoterComponent<Integer>(
                Arrays.asList(
                        new Voter<Integer>(){
                            @Override
                            public int vote(Integer obj) {
                                return obj >= 10 ?ACCESS_GRANTED : ACCESS_DENIED;
                            }
                        },
                        new Voter<Integer>(){
                            @Override
                            public int vote(Integer obj) {
                                return obj >= 30 ?ACCESS_GRANTED : ACCESS_DENIED;
                            }
                        }
                ), 0.6D
        );
        logger.info("{}",component.vote(39));
        logger.info("{}",component.vote(29));
        logger.info("{}",component.vote(19));
        logger.info("{}",component.vote(9));
    }
}