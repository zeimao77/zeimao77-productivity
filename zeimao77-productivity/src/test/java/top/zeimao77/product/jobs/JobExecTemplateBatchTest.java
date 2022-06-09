package top.zeimao77.product.jobs;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.tree.RandomVoter;
import top.zeimao77.product.tree.Voter;
import top.zeimao77.product.util.UuidGenerator;

import java.util.List;

class JobExecTemplateBatchTest extends BaseMain {

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

    public static class JobBatchTest extends JobExecTemplateBatch<Job> {

        private int i = 0;
        RandomVoter randomVoter = new RandomVoter(0.97);

        public JobBatchTest(int nMaxJobs) {
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
                over();  //没有更多任务时调用 将不会在调用 moreJob 方法
            }
        }

        @Override
        public int handle(List<Job> jobs) {
            for (Job job : jobs) {
                logger.info("正在处理任务({}):{}",++i,job.jobId);
            }
            if(randomVoter.vote(null) == Voter.ACCESS_GRANTED) {  // 模拟任务有3%的概率处理失败的场景
                return SUCCESSED;
            }
            return FAILED;
        }
    }

    @Test
    public void test() {
        JobBatchTest jobBatchTest = new JobBatchTest(16);
        jobBatchTest.prepare();
        jobBatchTest.start(3,3);
        logger.info("over");
    }


}