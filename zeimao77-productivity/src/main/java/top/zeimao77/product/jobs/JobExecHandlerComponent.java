package top.zeimao77.product.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JobExecHandlerComponent<T extends IJob> extends JobExecHandler<T> {

    private List<JobExecHandler<T>> jobExecHandlerList = new ArrayList<>();

    @Override
    public boolean support(T job, Map<String, Object> param) {
        return true;
    }

    @Override
    public Result doHandle(T job, Map<String, Object> param) {
        for (JobExecHandler<T> tJobExecHandler : jobExecHandlerList) {
            if(tJobExecHandler.support(job,param)) {
                tJobExecHandler.handle(job,param);
                return Result.SUCCESS;
            }
        }
        return Result.fail(FAILED,"没有找到任务的处理实现",null);
    }

    public boolean register(JobExecHandler<T> jobExecHandler) {
        return this.jobExecHandlerList.add(jobExecHandler);
    }

    public boolean unregister(JobExecHandler<T> jobExecHandler) {
        return this.jobExecHandlerList.remove(jobExecHandler);
    }

    public JobExecHandler<T> unregister(int index) {
        return this.jobExecHandlerList.remove(index);
    }

}
