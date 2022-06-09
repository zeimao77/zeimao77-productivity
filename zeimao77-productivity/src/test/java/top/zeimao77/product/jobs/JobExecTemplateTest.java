package top.zeimao77.product.jobs;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.tree.RandomVoter;
import top.zeimao77.product.tree.Voter;
import top.zeimao77.product.util.UuidGenerator;

class JobExecTemplateTest extends BaseMain {

    public static class Job implements IJob {
        private String jobId = UuidGenerator.INSTANCE.generate();
        private int consume = 2;

        public String jobId() {
            return this.jobId;
        }

        @Override
        public int consume() {
            return consume--;
        }
    }

    public static class JobTest extends JobExecTemplate {

        RandomVoter randomVoter = new RandomVoter(0.97);

        public JobTest(int nMaxJobs) {
            super(nMaxJobs);
        }

        @Override
        public void prepare() {
            logger.info("预备工作完成;");
            super.prepare();
        }

        @Override
        protected void moreJob(int page) {
            for (int i = 0; i < 10; i++) {
                addJob(new Job());
            }
            if(page == 10) {
                over();
            }
        }

        @Override
        public int handle(IJob job) {
            if(randomVoter.vote(null) == Voter.ACCESS_GRANTED) {
                return SUCCESSED;
            }
            return FAILED;
        }
    }

    @Test
    public void test() {
        JobTest jobTest = new JobTest(16);
        jobTest.prepare();
        jobTest.start(3);
        logger.info("over");
    }

}