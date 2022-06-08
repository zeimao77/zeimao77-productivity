package top.zeimao77.product.tree;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomVoter implements Voter<Void> {

    private int bound;
    private double seed;
    Random random;

    /**
     * 精度 1/1000000
     * @param seed 投票通过的概率
     */
    public RandomVoter(double seed) {
        this(seed,ThreadLocalRandom.current());
    }

    public RandomVoter(double seed,Random random) {
        if(seed > 0.999999D) {
            this.seed = 1;
        } else if(seed < 0.000001D) {
            this.seed = 0;
        } else {
            this.seed = seed;
            this.bound = (int) (this.seed * 1000000);
        }
        this.random = random;
    }

    @Override
    public int vote(Void obj) {
        if(this.seed == 1) {
            return ACCESS_GRANTED;
        }
        if(this.seed == 0) {
            return ACCESS_DENIED;
        }
        int r = this.random.nextInt(1000000);
        if(r < bound) {
            return ACCESS_GRANTED;
        }
        return ACCESS_DENIED;
    }

    public double getSeed() {
        return seed;
    }

}
