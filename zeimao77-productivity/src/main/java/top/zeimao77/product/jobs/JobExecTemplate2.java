package top.zeimao77.product.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

public class JobExecTemplate2<T extends IJob> implements JobExec{

    private static Logger logger = LoggerFactory.getLogger(JobExecTemplate2.class);

    protected ExecutorService executorService;
    protected JobExecHandler<T> jobExecHandler;
    protected Integer nMaxJobs;

    public JobExecTemplate2(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public JobExecTemplate2(int nMaxJobs) {
        this.nMaxJobs = nMaxJobs;
    }


    /**
     * 任务处理的扩展参数
     */
    protected Map<String,Object> jobParam;
    /**
     * 1:已准备好
     * 2:正在运行中
     * 4:运行结束停止
     * 5:请求立即停止任务,我们会在处理完当前任务之后立即停止
     */
    protected int status = 0;

    public boolean addJob(T t) {
        if(this.status != 2)
            return false;
        try {
            executorService.execute(()->{
                this.jobExecHandler.handle(t,jobParam);
            });
            return true;
        } catch (RejectedExecutionException e) {
            logger.debug("任务队列满");
            return false;
        }
    }

    /**
     * 如果线程池满 则休息一会继续尝试
     * @param t 任务
     * @param sleetStrategy 睡眠策略
     * @return true
     */
    public boolean addJob(T t, TokenBucket.SleetStrategy sleetStrategy) {
        while (!this.addJob(t)) {
            sleetStrategy.sleep();
        }
        return true;
    }

    public void start(int nThreads) {
        if(executorService == null)
            this.executorService = new ThreadPoolExecutor(nThreads,4 * nThreads,30
                    ,TimeUnit.SECONDS,new LinkedBlockingQueue(nMaxJobs));
        setStatus(2);
    }

    public JobExecHandler<T> getJobExecHandler() {
        return jobExecHandler;
    }

    public void setJobExecHandler(JobExecHandler<T> jobExecHandler) {
        this.jobExecHandler = jobExecHandler;
    }

    public Map<String, Object> getJobParam() {
        return jobParam;
    }

    public void setJobParam(Map<String, Object> jobParam) {
        this.jobParam = jobParam;
    }

    /**
     * 准备
     */
    public void prepare(){
        setStatus(1);
    }

    public int getStatus() {
        return status;
    }

    public synchronized void setStatus(int status) {
        this.status = status;
    }

    protected void over(){
        setStatus(5);
        this.executorService.shutdown();
        setStatus(4);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
