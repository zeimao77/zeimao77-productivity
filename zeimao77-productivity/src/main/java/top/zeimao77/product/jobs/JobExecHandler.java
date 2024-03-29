package top.zeimao77.product.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.exception.ExceptionCodeDefinition;

import java.util.Map;

public abstract class JobExecHandler<T extends IJob> implements JobExec{

    private static Logger logger = LoggerFactory.getLogger(JobExecHandler.class);

    /**
     * @param job 任务
     * @param param 扩展参数
     * @return 是否可以处理该任务
     * 需要注意的是不要让该方法抛出异常，否则可能会带来不可预测的结果;
     */
    public abstract boolean support(T job, Map<String,Object> param);

    /**
     * @param job 任务
     * @param param 扩展参数
     * @return 处理结果
     * 处理一个任务
     * 如果处理失败 我们希望您通过异常
     * @see BaseServiceRunException
     * 或者 top.zeimao77.product.jobs.JobExec.Result
     * 返回错误信息;
     */
    protected abstract Result doHandle(T job,Map<String,Object> param);

    /**
     * 处理任务
     * @see JobExecHandler#doHandle(IJob, Map)
     * 如果处理失败将调用
     * @see JobExecHandler#failed(IJob, Map, Result)
     * 如果处理成功将调用
     * @see JobExecHandler#successed(IJob, Map, Result)
     * @param job 任务
     * @param param 扩展参数
     */
    public void handle(T job, Map<String,Object> param) {
        try {
            if(support(job,param)) {
                Result result = doHandle(job,param);
                if(result.success())
                    successed(job,param,result);
                else
                    failed(job,param,result);
            }
        } catch (BaseServiceRunException e) {
            logger.error(String.format("[%s]%s",e.getCode(),e.getMessage()),e);
            Result fail = Result.fail(e.getCode(), e.getMessage(), e);
            failed(job,param,fail);
        } catch (RuntimeException e) {
            logger.error("JOB处理运行时错误",e);
            Result fail = Result.fail(FAILED, "业务处理错误", e);
            failed(job,param,fail);
        } catch (Throwable e) {
            logger.error("JOb处理受检查的错误",e);
            Result fail = Result.fail(FAILED, "未知的错误", e);
            failed(job,param,fail);
        }
    }

    /**
     * 失败处理方法
     * @param job 任务
     * @param param 扩展参数
     * @param result 调用结果;
     * 如果以异常的方式返回 Result#data 将是异常;
     */
    public void failed(T job,Map<String,Object> param,Result result) {
        logger.info("JOB({})处理失败,原因:[{}]{}",job.jobId(),result.getResultCode(),result.getResultMsg());
    }

    /**
     * 成功处理方法
     * @param job 任务
     * @param param 扩展参数
     * @param result 调用结果
     */
    public void successed(T job,Map<String,Object> param,Result result) {
        logger.info("JOB({})处理成功:[{}]{}",job.jobId(),result.getResultCode(),result.getResultMsg());
    }

    /**
     * 成功处理任务的后置处理
     * @param <T> 任务
     */
    public interface SuccessHandler<T>{
        void successed(T job,Map<String,Object> param,Result result);
    }

    /**
     * 失败处理任务的后置处理
     * @param <T>
     */
    public interface FailureHandler<T>{
        void failed(T job,Map<String,Object> param,Result result);
    }

}
