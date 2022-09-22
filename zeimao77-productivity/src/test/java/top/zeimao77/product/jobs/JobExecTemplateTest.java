package top.zeimao77.product.jobs;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.UuidGenerator;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

class JobExecTemplateTest extends BaseMain {

    /**
     * 任务模型
     */
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

    /**
     * 任务
     */
    public static class JobTest extends JobExecTemplate {

        public JobTest(int nMaxJobs) {
            super(nMaxJobs);
        }

        /**
         * 实始化模板实例
         */
        @Override
        public void prepare() {
            logger.info("预备工作完成;");
            super.prepare();
        }

        /**
         * 定义如何添加任务,如果任务池空了将调用该方法添加任务
         * 如果所有任务处理结束，调用over()函数
         * @param page 页码 每次调用该函数会增加1
         */
        @Override
        protected void moreJob(int page) {
            for (int i = 0; i < 10; i++) {
                addJob(new Job());
            }
            if(page == 13) {
                over();
            }
        }

    }

    @Test
    public void test() {
        JobTest jobTest = new JobTest(16);
        AtomicInteger a = new AtomicInteger(0);
        /**
         * 重要方法，定义如何消费任务
         */
        jobTest.setJobExecHandler(new JobExecHandler<IJob>() {
            @Override
            public boolean support(IJob job, Map<String, Object> param) {
                return true;
            }

            @Override
            protected Result doHandle(IJob job, Map<String, Object> param) {
                logger.info("job正在处理:{}",job.jobId());
                a.addAndGet(1);
                return Result.SUCCESS;
            }
        });
        jobTest.prepare();
        // 开启多少个线程处理任务
        jobTest.start(3);
        logger.info("over:{}",a.get());
    }

}