package top.zeimao77.product.exception;

/**
 * 不受检查的异常
 */
public class BaseServiceRunException extends RuntimeException implements ExceptionCodeDefinition {

    private Integer code;

    public BaseServiceRunException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BaseServiceRunException(String message, Throwable cause) {
        super(message,cause);
        this.code = CUSTOM;
    }

    public BaseServiceRunException(String message) {
        super(message);
        this.code = CUSTOM;
    }

    public Integer getCode() {
        return code;
    }

}
