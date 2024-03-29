package top.zeimao77.product.fileio.serialize;

import top.zeimao77.product.exception.BaseServiceRunException;

public enum Type {

    // 基本数据类型
    NULL(0x01)
    ,BYTES(0x02)
    ,CHAR(0x03)
    ,SHORT(0x04)
    ,BOOL(0x05)
    ,INT32(0x06)
    ,FLOAT(0x07)
    ,INT64(0x08)
    ,DOUBLE(0x09)
    ,STRING(0x0A)
    ,ARRAY(0x0B)
    ,MAP(0x0C)
    ,SET(0x0D)

    // 扩展数据类型
    ,ZIPSTRING(0x11)
    ,TIME(0x12)
    ,DATE(0x13)
    ,DATETIME(0x14)
    ,DECIMAL(0x15)

    // 自定义数据类型 建议的区间从0x70开始 0x00-0x6F建议先保留;
    ,CUS1(0x71);

    private byte typeValue;

    Type(int typeValue) {
        this.typeValue = (byte)typeValue;
    }

    public byte getTypeValue() {
        return typeValue;
    }

    public static Type parse(byte b) {
        switch (b) {
            case 0x01:
                return NULL;
            case 0x02:
                return BYTES;
            case 0x03:
                return CHAR;
            case 0x04:
                return SHORT;
            case 0x05:
                return BOOL;
            case 0x06:
                return INT32;
            case 0x07:
                return FLOAT;
            case 0x08:
                return INT64;
            case 0x09:
                return DOUBLE;
            case 0x0A:
                return STRING;
            case 0x0B:
                return ARRAY;
            case 0x0C:
                return MAP;
            case 0x0D:
                return SET;
            case 0x11:
                return ZIPSTRING;
            case 0x12:
                return TIME;
            case 0x13:
                return DATE;
            case 0x14:
                return DATETIME;
            case 0x15:
                return DECIMAL;
            default:
                throw new BaseServiceRunException("不能识别的自定义类型");
        }
    }

}
