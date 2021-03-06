package top.zeimao77.product.fileio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class TextFileUtil {

    private static Logger logger = LogManager.getLogger(TextFileUtil.class);

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
        try{
            FileInputStream in = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,cs));
            String s1 = reader.readLine();
            int lineNo = 1;
            while (s1 != null) {
                con.accept(lineNo,s1);
                s1 = reader.readLine();
                lineNo++;
            }
            reader.close();
            in.close();
        } catch (FileNotFoundException e) {
            logger.error("文件未找到",e);
        } catch (IOException e) {
            logger.error("读取文件失败",e);
        }
    }

    public static void lineConsumer(File file, BiConsumer<Integer,String> con) {
        lineConsumer(file,con,StandardCharsets.UTF_8);
    }

    // 文本文件的行消费
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
