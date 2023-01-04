package top.zeimao77.product.fileio.serialize;

public enum Type {

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
    ,TIME(0x0B)
    ,DATE(0x0C)
    ,DATETIME(0x0D)
    ,ZIPSTRING(0x0E)
    ,ARRAY(0x11)
    ,SET(0x12)
    ,MAP(0x13)
    ,DECIMAL(0x14)

    ,CUS1(0xA1);

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
                return TIME;
            case 0x0C:
                return DATE;
            case 0x0D:
                return DATETIME;
            case 0x11:
                return ARRAY;
            case 0x12:
                return SET;
            case 0x013:
                return MAP;
            default:
                return NULL;
        }
    }

}
