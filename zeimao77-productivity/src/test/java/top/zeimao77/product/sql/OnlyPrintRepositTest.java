package top.zeimao77.product.sql;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.util.StreamUtil;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

class OnlyPrintRepositTest {

    @Test
    public void update() {
        PrintWriter printWriter = new PrintWriter(System.out);
        OnlyPrintReposit onlyPrintReposit = new OnlyPrintReposit(printWriter);
        onlyPrintReposit.update("UPDATE abc set id = ?",new Object[]{"666"});
        printWriter.close();
    }


    @Test
    void selectByResolver() {
        PrintWriter printWriter = new PrintWriter(System.out);
        OnlyPrintReposit onlyPrintReposit = new OnlyPrintReposit(printWriter);
        onlyPrintReposit.updateByResolver(new SQL()
                .select("abc")
                .from("tab")
                .where(SQL.BIND_AND,"c1",SQL.COND_QIS,"1"));
        printWriter.close();
    }
}