package top.zeimao77.product.exception;

/**
 * 受检查的异常
 */
public class BaseServiceException extends Exception implements ExceptionCodeDefinition{

    private Integer code;

    /**
     * @param code 异常编码
     * @param message 异常信息
     * @param cause 异常堆栈
     */
    public BaseServiceException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * @param code 异常编码
     * @param message 异常信息
     */
    public BaseServiceException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * @return 异常编码
     */
    public Integer getCode() {
        return code;
    }

}
