package top.zeimao77.product.jobs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.exception.ExceptionCodeDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class JobExecTemplateBatch<T extends IJob> implements JobExec{

    private static Logger logger = LogManager.getLogger(JobExecTemplateBatch.class);

    protected ArrayBlockingQueue<T> jobs;
    protected ExecutorService executorService;
    // 状态
    // 1:已准备好
    // 2:正在运行中
    // 3:没有更多任务了，将不会再调用moreJob()
    // 4:运行结束停止
    // 5:请求立即停止任务,我们会在处理完当前任务之后立即停止
    protected int status = 0;

    public JobExecTemplateBatch(int nMaxJobs) {
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

    public abstract Result handle(List<T> jobs);

    /**
     * 失败了，如果
     * @see JobExecTemplateBatch#handle(java.util.List)
     * 处理失败，将调用此函数进行下一步处理
     * @param jobList
     */
    public void failed(List<T> jobList,Result result){
        if(result.retrieable()) {
            for (T job : jobList) {
                logger.warn("任务(ID:{})处理失败，即将重新处理!",job.jobId());
                if (job.consume() > 0) {
                    logger.warn("任务(ID:{})处理失败，即将重新处理",job.jobId());
                    addJob(job);
                } else {
                    logger.warn("任务(ID:{})处理失败，即将丢弃任务",job.jobId());
                }
            }
        } else {
            logger.warn("任务(ID:{})处理失败,失败原因:{},即将放弃处理!",jobList.get(0).jobId(),result.getResultMsg());
        }
    }

    public void successed(List<T> jobList) {
        for (T job : jobList) {
            logger.info("任务(ID:{})处理成功",job.jobId());
        }
    }

    public void start(int nThread,int pageSize) {
        start(nThread,240,TimeUnit.HOURS,pageSize);
    }

    /**
     * 开始任务 调用该方法会阻塞 直到任务执行完成
     * 如果任务执行超时或中断，剩余的任务将会被取消
     * 在执行过程中，可以通过调用 setStatus(5); 将结束任务，剩余的任务将被取消
     * @param nThreads 线程数
     * @param timeout 任务执行超时时间
     * @param unit 时间单位
     */
    public void start(int nThreads,long timeout, TimeUnit unit,int pageSize) {
        if(executorService == null) {
            executorService = Executors.newFixedThreadPool(nThreads);
        }
        setStatus(2);
        long start = System.currentTimeMillis();
        AtomicInteger page = new AtomicInteger(1);
        for (int i = 0; i < nThreads; i++) {
            executorService.execute(()->{
                ArrayList<T> jobList = new ArrayList<>(pageSize);
                while (status != 5) {
                    synchronized (JobExecTemplateBatch.class) {
                        int q = pageSize < jobs.size() ? pageSize : jobs.size();
                        for (int j = 0; j < q; j++) {
                            T job =jobs.poll();
                            if(job != null)
                                jobList.add(job);
                        }
                        if(this.jobs.isEmpty() && 2 == status) {
                            moreJob(page.getAndAdd(1));
                        }
                        q = pageSize < jobList.size() + jobs.size() ? pageSize : jobList.size() + this.jobs.size();
                        for (int j = jobList.size(); j < q; j++) {
                            T job =jobs.poll();
                            if(job != null)
                                jobList.add(job);
                        }
                    }
                    if(jobList.isEmpty()) {
                        logger.debug("线程({})没有取到更多任务，即将退出",Thread.currentThread().getName());
                        break;
                    }
                    try{
                        Result handle = handle(jobList);
                        if(handle == Result.SUCCESS || SUCCESSED == handle.getResultCode()) {
                            successed(jobList);
                        } else {
                            failed(jobList,handle);
                        }
                    } catch (BaseServiceRunException e) {
                        logger.error("任务处理异常",e);
                        Result fail = Result.fail(e.getCode(), e.getMessage());
                        failed(jobList,fail);
                    } catch (Exception e) {
                        logger.error("任务处理异常",e);
                        Result fail = Result.fail(ExceptionCodeDefinition.UNKNOWN,"未知的异常");
                        failed(jobList,fail);
                    }
                    jobList.clear();
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
