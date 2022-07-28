package top.zeimao77.product.exception;

import top.zeimao77.product.util.LongBitMap;

public interface ExceptionCodeDefinition extends LongBitMap {


    Integer NON_RETRYABLE = 0x40000000;  // 定义一个错误是不建议再次尝试的;
    Integer CONVERTIBLE = 0x20000000;  // 表示该错误由于捕获一个异常之后转化而来的异常
    Integer RETAION1 = 0x10000000;  // 保留标志1
    Integer RETAION2 = 0x08000000;  // 保留标志2
    Integer RETAION3 = 0x04000000;  // 保留标志3
    Integer RETAION4 = 0x02000000;  // 保留标志4
    Integer RETAION5 = 0x01000000;  // 保留标志5

    // 0x??000100 - 0x??0001FFF  // 手工直接抛出的异常;
    Integer UNKNOWN = 0x001000;   // 未知的异常;
    Integer CUSTOM = 0x001001;   // 自定义的异常;
    Integer WRONG_ACTION = 0x001002;  // 错误的操作;
    Integer NO_PERMISSION = 0x001003;  // 没有权限;
    Integer TRY_AGAIN_LATER = 0x001004; // 临时资源受限,建议稍后再试;
    Integer WRONG_SOURCE = 0x001005 | NON_RETRYABLE;   // 错误的参数或源;
    Integer NOT_SUPPORTED = 0x001006 | NON_RETRYABLE;   // 不支持的操作;

    Integer IOEXCEPTION = 0x002001 | CONVERTIBLE;  // IO错误异常;
    Integer SQLEXCEPTION = 0x002002 | CONVERTIBLE;  // SQL错误异常;



}
