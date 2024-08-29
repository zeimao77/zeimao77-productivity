package top.zeimao77.product.jobs;

import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.exception.ExceptionCodeDefinition;

public abstract class Retryable {

    public abstract void retryBefore(Throwable e);
    public abstract void doService();

    private int retryCount = 2;

    public void doTry() {
        for(int i = 0; i < retryCount; i++) {
            try {
                doService();
            } catch (BaseServiceRunException e) {
                if(i < retryCount-1 && e.matcheFlag(ExceptionCodeDefinition.RETRYABLE)) {
                    retryBefore(e);
                } else {
                    throw e;
                }
            }
        }
    }




}
