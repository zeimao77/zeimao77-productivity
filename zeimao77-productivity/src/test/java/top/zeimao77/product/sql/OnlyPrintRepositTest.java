package top.zeimao77.product.sql;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.util.StreamUtil;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

class OnlyPrintRepositTest {

    @Test
    public void update() {
        PrintWriter printWriter = StreamUtil.printWriter("C:\\Users\\zeimao77\\Desktop\\demo1.sql");
        OnlyPrintReposit onlyPrintReposit = new OnlyPrintReposit(printWriter);
        onlyPrintReposit.update("UPDATE abc set id = ?",new Object[]{"666"});
        printWriter.close();
    }


}