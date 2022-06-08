package top.zeimao77.product.jobs;

/**
 * @author zeimao77
 * @version 20220316.173400
 */
public interface IJob {

    /**
     * 执行任务之后判断
     * 如果返回小于等于0 将不再重试
     * 通常实现在上传失败之后在该函数将剩余重试次数减1
     * @return 剩余重试次数
     */
    default int consume() {
        return 0;
    }

    /**
     * @return jobId
     */
    default String jobId() {
        return "";
    }

}
