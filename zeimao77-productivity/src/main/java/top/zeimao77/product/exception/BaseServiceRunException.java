package top.zeimao77.product.exception;

/**
 * 不受检查的异常
 */
public class BaseServiceRunException extends RuntimeException implements ExceptionCodeDefinition {

    private Integer code;

    public BaseServiceRunException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code | CONVERTIBLE;
    }

    public BaseServiceRunException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BaseServiceRunException(String message, Throwable cause) {
        super(message,cause);
        this.code = APPERR | CONVERTIBLE;
    }

    public BaseServiceRunException(String message) {
        super(message);
        this.code = APPERR;
    }

    public Integer getCode() {
        return code;
    }

}
