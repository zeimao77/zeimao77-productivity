package top.zeimao77.product.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zeimao77
 * @version 2.0.9
 */
public abstract class JobExecTemplate<T extends IJob> implements JobExec{

    private static Logger logger = LoggerFactory.getLogger(JobExecTemplate.class);

    protected ArrayBlockingQueue<T> jobs;
    protected ExecutorService executorService;
    protected JobExecHandler<T> jobExecHandler;
    /**
     * 任务处理的扩展参数
     */
    protected Map<String,Object> jobParam;
    /**
     * 1:已准备好
     * @see JobExecTemplate#prepare()
     * 2:正在运行中
     * @see JobExecTemplate#start(int, long, TimeUnit)
     * 3:没有更多任务了，将不会再调用moreJob()
     * @see JobExecTemplate#moreJob(int)
     * 4:运行结束停止
     * @see JobExecTemplate#start(int, long, TimeUnit)
     * 5:请求立即停止任务,我们会在处理完当前任务之后立即停止
     * @see JobExecTemplate#setStatus(int)
     */
    protected int status = 0;

    /**
     *
     * @param executorService
     * @param nMaxJobs 单次调用添加任务的最大任务数
     */
    public JobExecTemplate(ExecutorService executorService,int nMaxJobs) {
        this.jobs = new ArrayBlockingQueue<>(nMaxJobs);
        this.executorService = executorService;
    }

    public JobExecTemplate(int nMaxJobs) {
        this.jobs = new ArrayBlockingQueue<>(nMaxJobs);
    }

    /**
     * 向任务队列添加任务，如果队列满会阻塞该进程
     * @param t 任务
     */
    public boolean addJob(T t) {
        try {
            jobs.put(t);
            return true;
        } catch (InterruptedException e) {
            logger.error("线程中断退出阻塞,可能丢失了任务JOBID:"+t.jobId(),e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * 为了防止因任务执行失败重新添加回任务列表导致阻塞
     * 我们建议您的最多添加任务个数为：
     * jobs.size() - threadPoolExecutor.getCorePoolSize()
     */
    protected abstract void moreJob(int page);

    /**
     * 开始任务 默认10天超时时间
     * @param nThread 线程数
     */
    public void start(int nThread) {
        start(nThread,240,TimeUnit.HOURS);
    }

    /**
     * 开始任务
     * 如果任务执行超时或中断，剩余的任务将会被取消
     * 在执行过程中，可以通过调用 setStatus(5); 将结束任务，剩余的任务将被取消
     * @param nThreads 线程数
     * @param timeout 任务执行超时时间
     * @param unit 时间单位
     */
    public void start(int nThreads,long timeout, TimeUnit unit) {
        if(executorService == null) {
            executorService = Executors.newFixedThreadPool(nThreads);
        }
        setStatus(2);
        long start = System.currentTimeMillis();
        AtomicInteger page = new AtomicInteger(1);
        for (int i = 0; i < nThreads; i++) {
            executorService.execute(()->{
                while (status != 5) {
                    T job =jobs.poll();
                    if(job == null) {
                        synchronized (this) {
                            if(jobs.isEmpty() && 2 == status) {
                                moreJob(page.getAndAdd(1));
                            }
                            job = jobs.poll();
                        }
                    }
                    if(job == null) {
                        logger.debug("线程({})没有取到更多任务，即将退出",Thread.currentThread().getName());
                        break;
                    }
                    jobExecHandler.handle(job,jobParam);
                }
            });
        }
        executorService.shutdown();
        try {
            if(!executorService.awaitTermination(timeout, unit)) {
                logger.error("线程池执行超时,剩余任务将被取消");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.error("线程中断退出阻塞",e);
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        long end = System.currentTimeMillis();
        logger.info("任务执行结束，总共耗时:{}毫秒;",(end-start));
        if(status != 5) {
            setStatus(4);
        }
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

    public synchronized void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    /**
     * 没有更多任务了，将不会再调用moreJob()
     */
    protected void over(){
        setStatus(3);
    }

}
