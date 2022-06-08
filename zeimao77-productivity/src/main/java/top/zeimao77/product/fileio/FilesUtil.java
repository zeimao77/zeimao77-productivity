package top.zeimao77.product.fileio;

import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.util.ByteArrayCoDesUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Consumer;

/**
 * 文件目录处理工具
 */
public class FilesUtil {

    /**
     * 递归消费某一个目录
     * @param dirPath  目录路径
     * @param con 消费者
     */
    public static void forDir(String dirPath,Consumer<File> con) {
        Path path = Paths.get(dirPath);
        try {
            Files.walkFileTree(path,new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    con.accept(file.toFile());
                    return super.visitFile(file, attrs);
                }
            });
        } catch (IOException e) {
            throw new BaseServiceRunException("IO异常",e);
        }
    }

    /**
     * 对文件MD5摘要
     * @param file 文件
     * @return 摘要结果
     */
    public static String md5(File file){
        return ByteArrayCoDesUtil.hexEncode(digest(file,"MD5"));
    }

    /**
     * 对文件SHA摘要
     * @param file 文件
     * @return 摘要结果
     */
    public static String sha(File file){
        return ByteArrayCoDesUtil.hexEncode(digest(file,"SHA"));
    }

    /**
     * 对文件进行摘要
     * @param file 文件
     * @param algorithm  摘要算法 MD5/SHA
     * @return 摘要结果
     */
    public static byte[] digest(File file,String algorithm){
        try (FileInputStream in = new FileInputStream(file);){
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance(algorithm);
            md5.update(byteBuffer);
            return md5.digest();
        }catch (FileNotFoundException e) {
            throw new BaseServiceRunException("文件未找到错误",e);
        } catch (IOException e) {
            throw new BaseServiceRunException("IO错误",e);
        } catch (NoSuchAlgorithmException e) {
            throw new BaseServiceRunException("algorithm算法错误",e);
        }
    }

}
