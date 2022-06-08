package top.zeimao77.product.util;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import java.io.*;
import java.nio.charset.StandardCharsets;

class StreamUtilTest extends BaseMain {

    public static final String path = "E:\\test.log";

    @Test
    void printWriter() {
        int buff = 1 << 13;
        long start = System.currentTimeMillis();
        PrintWriter printWriter = StreamUtil.printWriter(path, buff, false, StandardCharsets.UTF_8);
        RandomStringUtil stringUtil = new RandomStringUtil(0x0F);
        for (int i = 0; i < 102400; i++) {
            String s = stringUtil.randomStr(1024);
            printWriter.println(s);
        }
        logger.info("文件写完了，缓存设置：{};耗时：{} 毫秒;",buff,(System.currentTimeMillis() - start));
    }


    @Test
    public void zipTest() throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream("hello world!!!!!!!!".getBytes(StandardCharsets.UTF_8));
        FileOutputStream fos = new FileOutputStream("E:\\te.zip");
        StreamUtil.zip(fos,bis,"/hello.txt");
        bis.close();
        fos.close();
    }

}