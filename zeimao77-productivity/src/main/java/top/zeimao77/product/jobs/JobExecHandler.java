package top.zeimao77.product.jobs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.exception.ExceptionCodeDefinition;

import java.util.Map;

public abstract class JobExecHandler<T extends IJob> implements JobExec{

    private static Logger logger = LogManager.getLogger(JobExecHandler.class);

    public abstract boolean support(T job, Map<String,Object> param);
    protected abstract Result doHandle(T job,Map<String,Object> param);

    public void handle(T job, Map<String,Object> param) {
        try {
            Result result = doHandle(job,param);
            if(result.success()) {
                successed(job,param,result);
            } else {
                failed(job,param,result);
            }
        } catch (BaseServiceRunException e) {
            Result fail = Result.fail(e.getCode(), e.getMessage(), e);
            failed(job,param,fail);
        } catch (RuntimeException e) {
            logger.error("JOB处理出错",e);
            Result fail = Result.fail(ExceptionCodeDefinition.UNKNOWN, "未知的错误", e);
            failed(job,param,fail);
        }
    }

    public void failed(T job,Map<String,Object> param,Result result) {
        logger.info("JOB处理失败,原因:[{}]{}",job.jobId(),result.getResultCode(),result.getResultMsg());
    }

    public void successed(T job,Map<String,Object> param,Result result) {
        logger.info("JOB处理成功:{}",job.jobId());
    }





}
