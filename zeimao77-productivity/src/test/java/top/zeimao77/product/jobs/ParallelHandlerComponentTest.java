package top.zeimao77.product.jobs;

import top.zeimao77.product.main.BaseMain;

import java.util.Map;

class ParallelHandlerComponentTest extends BaseMain {

    public static void main(String[] args) {
        ParallelHandlerComponent<JobExecTemplateTest.Job> jobParallelHandlerComponent = new ParallelHandlerComponent<>();
        jobParallelHandlerComponent.register(new JobExecHandler<JobExecTemplateTest.Job>() {
            @Override
            public boolean support(JobExecTemplateTest.Job job, Map<String, Object> param) {
                return true;
            }

            @Override
            protected Result doHandle(JobExecTemplateTest.Job job, Map<String, Object> param) {
                return Result.SUCCESS;
            }
        });
        jobParallelHandlerComponent.register(new JobExecHandler<JobExecTemplateTest.Job>() {
            @Override
            public boolean support(JobExecTemplateTest.Job job, Map<String, Object> param) {
                return true;
            }

            @Override
            protected Result doHandle(JobExecTemplateTest.Job job, Map<String, Object> param) {
                return Result.SUCCESS;
            }
        });
        for (int i = 0; i < 8; i++) {
            jobParallelHandlerComponent.handle(new JobExecTemplateTest.Job(),null);
        }
        jobParallelHandlerComponent.getExecutors().shutdown();
    }

}