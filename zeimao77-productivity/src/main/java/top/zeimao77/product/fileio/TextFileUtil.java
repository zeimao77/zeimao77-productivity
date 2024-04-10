package top.zeimao77.product.fileio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.exception.BaseServiceRunException;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.IOEXCEPTION;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.WRONG_SOURCE;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class TextFileUtil {

    private static Logger logger = LoggerFactory.getLogger(TextFileUtil.class);

    private TextFileUtil(){}

    /**
     * 将一个文本读取到字符串
     * @param filePath 文件路径
     * @param keepWrapping 是否保留换行符
     * @return 解析文件的结果
     */
    public static String readFile(String filePath,boolean keepWrapping){
        File file = new File(filePath);
        return readFile(file,keepWrapping);
    }

    public static String readFile(File file,boolean keepWrapping){
        StringBuilder sBuilder = new StringBuilder();
        BiConsumer<Integer,String> con = (o1,o2) -> {
            sBuilder.append(o2);
            if(keepWrapping) sBuilder.append("\n");
        };
        lineConsumer(file,con);
        return sBuilder.toString();
    }

    /**
     * 行消费某一个文本文件
     * @param file 文件
     * @param con 消费者
     */
    public static void lineConsumer(File file, BiConsumer<Integer,String> con, Charset cs) {
        try(
                FileInputStream in = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in,cs));
        ) {
            String s1 = reader.readLine();
            int lineNo = 1;
            while (s1 != null) {
                con.accept(lineNo,s1);
                s1 = reader.readLine();
                lineNo++;
            }
        } catch (FileNotFoundException e) {
            throw new BaseServiceRunException(WRONG_SOURCE,"文件未找到",e);
        } catch (IOException e) {
            throw new BaseServiceRunException(IOEXCEPTION,"IO错误",e);
        }
    }

    public static void lineConsumer(File file, BiConsumer<Integer,String> con) {
        lineConsumer(file,con,StandardCharsets.UTF_8);
    }

    /**
     * 消费文件的第一行
     * @param filePath 文本文件路径
     * @param con 行消费函数
     */
    public static void lineConsumer(String filePath, BiConsumer<Integer,String> con) {
        File file = new File(filePath);
        lineConsumer(file,con);
    }

    public static ArrayList<String> readLines(File file) {
        ArrayList<String> lines = new ArrayList<>();
        lineConsumer(file,(o1,o2)->lines.add(o2));
        return lines;
    }

}
