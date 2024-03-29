package top.zeimao77.product.jobs;

import java.util.ArrayList;
import java.util.function.BiFunction;

/**
 * 抽象的JOB实现 默认尝试一次
 */
public abstract class AbstractJob implements IJob{

    /**
     * 尝试处理次数 包含第一次尝试
     */
    protected int retry = 0;
    private ArrayList<JobExec.Result> errorList = new ArrayList<>();

    public ArrayList<JobExec.Result> getErrorList() {
        return errorList;
    }

    public void addError(JobExec.Result error) {
        this.errorList.add(error);
    }

    public JobExec.Result firstError() {
        for (JobExec.Result result : errorList) {
            if(result.getResultCode() != JobExec.SUCCESSED)
                return result;
        }
        return null;
    }

    public void clearError() {
        this.errorList.clear();
    }

    public void assertOk(BiFunction<Integer,String,RuntimeException> fun) {
        JobExec.Result result = firstError();
        if(result != null)
            throw fun.apply(result.getResultCode(),result.getResultMsg());
    }

    @Override
    public synchronized int consume() {
        return --retry;
    }

}
