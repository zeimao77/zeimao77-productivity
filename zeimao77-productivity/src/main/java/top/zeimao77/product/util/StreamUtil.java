package top.zeimao77.product.util;

import top.zeimao77.product.exception.BaseServiceRunException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class StreamUtil {

    private StreamUtil(){}

    /**
     * 按UTC-8编码读取输入流中的字符串
     * @param inputStream 输入流
     * @return 字符串
     */
    public static String readStream(InputStream inputStream) {
        return readStream(inputStream, StandardCharsets.UTF_8);
    }

    /**
     * 从流中读取字符串
     * @param inputStream 读取的流
     * @param charset 字符编码
     * @return 字符串
     */
    public static String readStream(InputStream inputStream,Charset charset) {
        String result = new BufferedReader(new InputStreamReader(inputStream,charset))
                .lines().collect(Collectors.joining("\n"));
        return result;
    }

    /**
     * BufferedWriter 8192的缓存在通常情况下效果还是很不错的
     * 在这里进行了适当的调大 32768
     * @param path 输出文件路径
     * @return 格式化的文本输出
     */
    public static PrintWriter printWriter(final String path) {
        return printWriter(path, 0x8000,false,StandardCharsets.UTF_8);
    }

    /**
     *
     * @param path 输出文件路径
     * @param size 缓冲区大小
     * @param append 是否追加，如果为true则文件需要已存在
     * @param cs 字符编码设置
     * @return 格式化的文本输出
     */
    public static PrintWriter printWriter(final String path,int size,boolean append,Charset cs) {
        try {
            File file = Paths.get(path).toFile();
            FileOutputStream outputStream = new FileOutputStream(file,append);
            OutputStreamWriter fileWriter = new OutputStreamWriter(outputStream,cs);
            BufferedWriter writer = new BufferedWriter(fileWriter,size);
            PrintWriter printWriter = new PrintWriter(writer,false);
            return printWriter;
        } catch (FileNotFoundException e) {
            throw new BaseServiceRunException("文件未找到");
        }
    }

    /**
     * 单个文件压缩
     * @param os 压缩文件输出流
     * @param is 被压缩文件输入
     * @param path 压缩后路径
     */
    public static void zip(OutputStream os,InputStream is,String path) {
        byte[] buf = new byte[4096];
        int l;
        try(ZipOutputStream zipos = new ZipOutputStream(os)){
            zipos.putNextEntry(new ZipEntry(path));
            while ((l = is.read(buf)) != -1) {
                zipos.write(buf,0,l);
            }
            zipos.closeEntry();
        } catch (IOException e) {
            throw new BaseServiceRunException("IO错误",e);
        }
    }

}
