package top.zeimao77.product.sql;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

class SQLTest extends BaseMain {

    @Test
    void select() {
        SQL select = new SQL().select()
                .from("abc", "a")
                .innerJoin("def", "b")
                .on(SQL.BIND_AND, "a.id", "b.id")
                .innerJoin("ghi", "g")
                .on(SQL.BIND_AND, "g.id", "a.id")
                .on(SQL.BIND_AND, "a.id", "g.id")
                .where(true,SQL.BIND_AND, "a.id", SQL.COND_QIS, "100");
        logger.info(select.getExecSql());


    }
}