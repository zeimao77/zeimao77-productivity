package top.zeimao77.product.exception;

import top.zeimao77.product.util.LongBitMap;

/**
 * 为了避免与系统错误冲突
 * 应用级别自定义错误码建议定义范围:[0x060001,0x08FFFF];
 */
public interface ExceptionCodeDefinition extends LongBitMap {

    Integer NON_RETRYABLE = 0x40000000;  // 定义一个错误是不建议再次尝试的;
    Integer CONVERTIBLE = 0x20000000;  // 表示该错误由于捕获一个异常之后转化而来的异常;
    Integer RETAION1 = 0x10000000;  // 保留标志1
    Integer RETAION2 = 0x08000000;  // 保留标志2
    Integer RETAION3 = 0x04000000;  // 保留标志3
    Integer RETAION4 = 0x02000000;  // 保留标志4
    Integer RETAION5 = 0x01000000;  // 保留标志5

    Integer UNKNOWN = 0x001000;   // 未知的异常(4096);
    Integer CUSTOM = 0x001001;   // 自定义的异常(4097);
    Integer APPERR = 0x060000;   // APP错误(393216);
    Integer WRONG_ACTION = 0x001002;  // 错误的操作(4098);
    Integer NO_PERMISSION = 0x001003;  // 没有权限(4099);
    Integer TRY_AGAIN_LATER = 0x001004; // 临时资源受限,建议稍后再试(4100);
    Integer WRONG_SOURCE = 0x001005;   // 错误的参数或源(4101);
    Integer NOT_SUPPORTED = 0x001006;   // 不支持的操作(4102);

    Integer IOEXCEPTION = 0x002001;  // IO错误异常(8193);
    Integer SQLEXCEPTION = 0x002002;  // SQL错误异常(8194);
    Integer SQLICVEXCEPTION = 0x002003;  // SQL数据违反完整性约束(8195);

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
