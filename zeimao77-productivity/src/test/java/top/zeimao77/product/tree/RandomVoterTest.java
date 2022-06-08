package top.zeimao77.product.tree;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

class RandomVoterTest extends BaseMain {

    @Test
    public void test() {
        BaseMain.showBanner();
        RandomVoter randomVoter = new RandomVoter(0.001);
        int c = 0;
        for (int i = 0; i < 10000; i++) {
            if(randomVoter.vote(null) == Voter.ACCESS_GRANTED) {
                c++;
            }
        }
        logger.info("C:{}",c);
    }

}