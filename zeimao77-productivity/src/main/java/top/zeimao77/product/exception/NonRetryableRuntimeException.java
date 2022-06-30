package top.zeimao77.product.exception;

/**
 * 不可重试的异常
 */
public class NonRetryableRuntimeException extends RuntimeException implements ExceptionCodeDefinition {

    private Integer code;

    public NonRetryableRuntimeException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code | NON_RETRYABLE;
    }

    public NonRetryableRuntimeException(String message, Throwable cause) {
        super(message,cause);
        this.code = CUSTOM | NON_RETRYABLE;
    }

    public NonRetryableRuntimeException(String message) {
        super(message);
        this.code = CUSTOM | NON_RETRYABLE;
    }

    public Integer getCode() {
        return code;
    }

}
