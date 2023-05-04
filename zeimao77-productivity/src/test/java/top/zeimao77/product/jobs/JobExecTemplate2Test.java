package top.zeimao77.product.jobs;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class JobExecTemplate2Test extends BaseMain {

    @Test
    void addJob() {
        JobExecTemplate2 jobExecTemplate2 = new JobExecTemplate2<JobExecTemplateTest.Job>(1);
        jobExecTemplate2.setJobExecHandler(new JobExecHandler() {
            @Override
            public boolean support(IJob job, Map param) {
                return true;
            }

            @Override
            protected Result doHandle(IJob job, Map param) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("正在处理：{}",job.jobId());
                return Result.SUCCESS;
            }
        });
        jobExecTemplate2.start(1);
        TokenBucket.SleetStrategy sleetStrategy = new TokenBucket.SleetStrategy(1,TimeUnit.SECONDS);
        for (int i = 0; i < 10; i++)
            jobExecTemplate2.addJob(new JobExecTemplateTest.Job(),sleetStrategy);

        jobExecTemplate2.over();
    }
}