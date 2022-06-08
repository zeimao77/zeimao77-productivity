package top.zeimao77.product.exception;

/**
 * 不受检查的异常
 */
public class BaseServiceRunException extends RuntimeException{

    /**
     * 不确定、未知的异常类型编码
     */
    public static final Integer UNKNOWN = 10000;
    /**
     * 自定义的异常类型编码
     */
    public static final Integer CUSTOM = 10001;

    private Integer code;

    public BaseServiceRunException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BaseServiceRunException(String message, Throwable cause) {
        super(message,cause);
        this.code = 10001;
    }

    public BaseServiceRunException(String message) {
        super(message);
        this.code = CUSTOM;
    }

    public Integer getCode() {
        return code;
    }

}
