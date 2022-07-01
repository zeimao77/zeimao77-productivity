package top.zeimao77.product.exception;

/**
 * 不可重试的异常
 */
public class NonRetryableRuntimeException extends BaseServiceRunException {

    public NonRetryableRuntimeException(Integer code, String message, Throwable cause) {
        super(code | NON_RETRYABLE,message, cause);
    }

    public NonRetryableRuntimeException(Integer code,String message) {
        super(code | NON_RETRYABLE,message);
    }

    public NonRetryableRuntimeException(String message, Throwable cause) {
        super(CUSTOM | NON_RETRYABLE,message,cause);
    }

    public NonRetryableRuntimeException(String message) {
        super(CUSTOM | NON_RETRYABLE,message);
    }

}
