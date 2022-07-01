package top.zeimao77.product.util;

import top.zeimao77.product.exception.BaseServiceRunException;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.IOEXCEPTION;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 字节数组编解码工具
 */
public class ByteArrayCoDesUtil {

    private ByteArrayCoDesUtil(){}

    /**
     * 将一个字节数组转十六进制字符串
     * @param bs 字节数组
     * @return 十六进制字符串
     */
    public static String hexEncode(byte[] bs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bs.length; i++) {
            String hex = hexEncode(bs[i]);
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 十六进制编码
     * @param b 字节
     * @return 十六进制结果
     */
    public static String hexEncode(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex;
    }

    /**
     * 将十六进制字符串转字节数组
     * @param str 解析的字符串
     * @return 解析后的字节码结果
     */
    public static byte[] hexDecode(String str) {
        int len = str.length();
        byte[] data = new byte[len << 1];
        for (int i = 0;i < len; i+= 2) {
            data[i << 1] = (byte) ((Character.digit(str.charAt(i),16) << 4)
                    + Character.digit(str.charAt(i+1),16));
        }
        return data;
    }

    /**
     * 将一个字节数组转BASE64字符串
     * @param bs 源字节数组
     * @return 转换后的BASE64字符串结果
     */
    public static String base64Encode(byte[] bs) {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] b = encoder.encode(bs);
        return new String(b);
    }


    /**
     * BASE64解码
     * @param str BASE64字符串
     * @return 结果
     */
    public static byte[] base64Decode(String str) {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(str);
    }

    /**
     * 生成一个数字校验码;范围:[0,255]
     * @param bs 字节数组
     * @return 检验码
     */
    public static int checkCode(byte[] bs) {
        int i = 0;
        byte result = 0x00;
        for (byte b : bs) {
            i = (result + b) & 0x07;
            result ^= ((b >> (8 - i) | (b << i)));
        }
        return result + 0x80;
    }

    public static boolean matchesCheckCode(byte[] bs,int checkCode) {
        int i = checkCode(bs);
        if(i == checkCode) {
            return true;
        }
        return false;
    }

    /**
     * 将一个字节数组ZIP编码
     * @param bs 字节数组
     * @return 编码后的字节数组
     */
    public static byte[] zipEncode(byte[] bs) {
        byte[] result = null;
        try(
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream zos = new ZipOutputStream(baos);
            ) {
            ZipEntry entry = new ZipEntry("data");
            entry.setSize(bs.length);
            zos.putNextEntry(entry);
            zos.write(bs);
            zos.closeEntry();
            result = baos.toByteArray();
        } catch (IOException e) {
            throw new BaseServiceRunException(IOEXCEPTION,"IO错误",e);
        }
        return result;
    }

    /**
     * 将ZIP编码后的字节码解码
     * @param zbs ZIP编码后的字节数组
     * @return 解码后的字节数组
     */
    public static byte[] zipDecode(byte[] zbs) {
        byte[] bs = null;
        int l = -1;
        try(
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ByteArrayInputStream bais = new ByteArrayInputStream(zbs);
                ZipInputStream zis = new ZipInputStream(bais);
            ) {
            if(zis.getNextEntry() != null) {
                byte[] buf = new byte[4096];
                while ((l = zis.read(buf, 0, buf.length)) != -1) {
                    baos.write(buf, 0, l);
                }
                bs = baos.toByteArray();
            }
        } catch (IOException e) {
            throw new BaseServiceRunException(IOEXCEPTION,"IO错误",e);
        }
        return bs;
    }

    /**
     * 对两个字节数组做异或运算，长度可以不一致
     * @param bs0 字节数组0
     * @param bs1 字节数组1
     * @return 异或结果
     */
    public static byte[] xor(byte[] bs0,byte[] bs1) {
        byte[] bsl = bs0.length > bs1.length ? bs0 : bs1;
        byte[] bss = bsl == bs0 ? bs1 : bs0;
        byte[] result = new byte[bsl.length];
        for (int i = 0; i < bsl.length; i++) {
            result[i] = bsl[i];
            if(i < bss.length) {
                result[i] ^= bss[i];
            }
        }
        return result;
    }

    public static byte[] toByteArray(Long aLong) {
        ByteBuffer allocate = ByteBuffer.allocate(8);
        ByteBuffer byteBuffer = allocate.putLong(aLong);
        return byteBuffer.array();
    }

    public static byte[] toByteArray(Integer aInt) {
        ByteBuffer allocate = ByteBuffer.allocate(4);
        ByteBuffer byteBuffer = allocate.putInt(aInt);
        return byteBuffer.array();
    }

    public static byte[] toByteArray(Double aDou) {
        ByteBuffer allocate = ByteBuffer.allocate(8);
        ByteBuffer byteBuffer = allocate.putDouble(aDou);
        return byteBuffer.array();
    }

}
