package top.zeimao77.product.jobs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 顺序的处理器组件
 * 对于一个任务，将依次换处理器顺序处理该任务
 * @param <T>
 */
public class SequentialHandlerComponent<T extends IJob> extends JobExecHandler<T> {

    private static Logger logger = LogManager.getLogger(SequentialHandlerComponent.class);
    private List<JobExecHandler<T>> jobExecHandlerList = new ArrayList<>();

    @Override
    public boolean support(T job, Map<String, Object> param) {
        return true;
    }

    @Override
    public Result doHandle(T job, Map<String, Object> param) {
        for (JobExecHandler<T> tJobExecHandler : jobExecHandlerList) {
            boolean support = false;
            try {
                support = tJobExecHandler.support(job,param);
            }catch (Exception e) {
                logger.error("{}支持错误,任务({})可能丢失;",tJobExecHandler,job.jobId());
                logger.error("错误",e);
            }
            if(support) {
                tJobExecHandler.handle(job,param);
            }
        }
        return Result.SUCCESS;
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

    @Override
    public void successed(T job, Map<String, Object> param, Result result) {}


}
