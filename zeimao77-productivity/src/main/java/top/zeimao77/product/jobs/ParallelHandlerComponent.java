package top.zeimao77.product.jobs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 非阻塞的处理
 * 如果判断为需要处理，将开始一个新的线程利用该处理器处理该任务;
 * @param <T>
 */
public class ParallelHandlerComponent<T extends IJob> extends JobExecHandler<T> {

    private static Logger logger = LogManager.getLogger(ParallelHandlerComponent.class);
    ExecutorService executors;
    private List<JobExecHandler<T>> jobExecHandlerList = new ArrayList<>();

    public ParallelHandlerComponent(ExecutorService executors) {
        this.executors = executors;
    }

    public ParallelHandlerComponent() {
        this.executors = new ThreadPoolExecutor(2,8
                ,30, TimeUnit.SECONDS,new LinkedBlockingQueue(1024));
    }

    @Override
    public boolean support(T job, Map<String, Object> param) {
        return true;
    }

    @Override
    protected Result doHandle(T job, Map<String, Object> param) {
        for (JobExecHandler<T> tJobExecHandler : jobExecHandlerList) {
            boolean support = false;
            try {
                support = tJobExecHandler.support(job,param);
            }catch (Exception e) {
                logger.error("{}支持出错;",tJobExecHandler,e);
            }
            if(support) {
                executors.submit(() -> tJobExecHandler.handle(job,param));
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

    public ExecutorService getExecutors() {
        return executors;
    }

}
