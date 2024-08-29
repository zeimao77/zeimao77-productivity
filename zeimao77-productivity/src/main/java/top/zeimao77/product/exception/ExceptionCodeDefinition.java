package top.zeimao77.product.exception;

/**
 * 为了避免与系统错误冲突
 * 应用级别自定义错误码建议定义范围:[0x00A001/40961,0xFFFFFF/16777215];
 */
public interface ExceptionCodeDefinition {

    Integer RETRYABLE = 0x40000000;  // 定义一个错误是可以再次尝试操作;
    Integer CONVERTIBLE = 0x20000000;  // 该错误由于捕获一个异常之后转化而来的异常;
    Integer RETAION1 = 0x10000000;  // 保留标志1
    Integer RETAION2 = 0x08000000;  // 保留标志2
    Integer RETAION3 = 0x04000000;  // 保留标志3
    Integer RETAION4 = 0x02000000;  // 保留标志4
    Integer RETAION5 = 0x01000000;  // 保留标志5

    Integer APPERR = 0x00A000;   // APP错误(40960);
    Integer UNKNOWN = 0x001000;   // 未知的异常(4096);
    Integer CUSTOM = 0x001001;   // 自定义的异常(4097);
    Integer WRONG_ACTION = 0x001002;  // 错误的操作(4098);
    Integer NO_PERMISSION = 0x001003;  // 没有权限(4099);
    Integer TRY_AGAIN_LATER = 0x001004; // 临时资源受限(4100);
    Integer WRONG_SOURCE = 0x001005;   // 错误的参数或源(4101);
    Integer NOT_SUPPORTED = 0x001006;   // 不支持的操作(4102);

    Integer IOEXCEPTION = 0x002001;  // IO错误异常(8193);
    Integer SQLEXCEPTION = 0x002002;  // SQL错误异常(8194);
    Integer SQLICVEXCEPTION = 0x002003;  // SQL数据违反完整性约束(8195);
    Integer INTERRUPTED = 0x002004;    // 线程中断;

    Integer getCode();

    default boolean matcheFlag(Integer flag) {
        return (getCode() & flag) == flag;
    }
    default boolean matcheOriginError(Integer errorCode) {
        Integer i = (getCode() & 0x00FFFFFF) ^ errorCode;
        return i.equals(0);
    }
    default Integer originError() {
        return getCode() & 0x00FFFFFF;
    }

}
