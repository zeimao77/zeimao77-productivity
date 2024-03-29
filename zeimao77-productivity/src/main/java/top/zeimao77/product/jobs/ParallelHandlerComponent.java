package top.zeimao77.product.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.TRY_AGAIN_LATER;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 非阻塞的处理
 * 如果判断为需要处理，将开始一个新的线程利用该处理器处理该任务;
 * @param <T>
 */
public class ParallelHandlerComponent<T extends IJob> extends JobExecHandler<T> {

    private static Logger logger = LoggerFactory.getLogger(ParallelHandlerComponent.class);
    ExecutorService executors;
    private List<JobExecHandler<T>> jobExecHandlerList = new ArrayList<>();

    public ParallelHandlerComponent(ExecutorService executors) {
        this.executors = executors;
    }

    public ParallelHandlerComponent() {
        this.executors = new ThreadPoolExecutor(4,8
                ,30, TimeUnit.SECONDS,new LinkedBlockingQueue(1024)
        );
    }

    @Override
    public boolean support(T job, Map<String, Object> param) {
        return true;
    }

    @Override
    protected Result doHandle(T job, Map<String, Object> param) {
        for (JobExecHandler<T> tJobExecHandler : jobExecHandlerList) {
            try {
                executors.execute(() -> tJobExecHandler.handle(job,param));
            }catch (RejectedExecutionException e) {
                return Result.fail(TRY_AGAIN_LATER,"线程池拒绝了提交任务",e);
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

    public void shutdown() {
        this.executors.shutdown();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return this.executors.awaitTermination(timeout,unit);
    }

    @Override
    public void successed(T job, Map<String, Object> param, Result result) {}
}
