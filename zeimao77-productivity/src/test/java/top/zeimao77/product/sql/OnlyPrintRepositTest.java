package top.zeimao77.product.sql;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.mysql.DemoModel;
import top.zeimao77.product.util.StreamUtil;

import java.io.PrintWriter;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class OnlyPrintRepositTest {

    @Test
    public void update() {
        PrintWriter printWriter = new PrintWriter(System.out);
        OnlyPrintReposit onlyPrintReposit = new OnlyPrintReposit(printWriter);
        onlyPrintReposit.update("UPDATE abc set id = ?",new Object[]{"666"});
        HashMap<String,String> s = new HashMap<>();
        s.put("id","666");
        s.put("name","666");
        s.put("age","666");
        onlyPrintReposit.updateTable("abc",s,null,new String[]{"id","name"});
        printWriter.close();

    }


    @Test
    void selectByResolver() {
        PrintWriter printWriter = new PrintWriter(System.out);
        OnlyPrintReposit onlyPrintReposit = new OnlyPrintReposit(printWriter);
        onlyPrintReposit.updateByResolver(new SQL()
                .select("abc")
                .from("tab")
                .where(SQL.BIND_AND,"c1",SQL.COND_QIS,"1")
                .where(SQL.BIND_AND,"c2",SQL.COND_QNULL,null)
        );
        printWriter.close();
    }
}