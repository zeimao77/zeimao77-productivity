package top.zeimao77.product.exception;

/**
 * 不可重试的异常
 */
public class RetryableRuntimeException extends BaseServiceRunException {

    public RetryableRuntimeException(Integer code, String message, Throwable cause) {
        super(code | RETRYABLE,message, cause);
    }

    public RetryableRuntimeException(Integer code, String message) {
        super(code | RETRYABLE,message);
    }

    public RetryableRuntimeException(String message, Throwable cause) {
        super(APPERR | RETRYABLE | CONVERTIBLE,message,cause);
    }

    public RetryableRuntimeException(String message) {
        super(APPERR | RETRYABLE,message);
    }

}
