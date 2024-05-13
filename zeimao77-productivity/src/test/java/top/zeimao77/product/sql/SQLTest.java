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
                .where(true,SQL.BIND_AND, "a.id", SQL.COND_QIS, "100");
        logger.info(select.getExecSql());
        SQL insert = new SQL().insert("abc")
                .addValues("a", "val_a")
                .addValues("b", "val_b")
                .addValues("c", "val_c")
                .endValues();
        logger.info(insert.getExecSql());
    }

    @Test
    void testbracket() {
        SQL select = new SQL().select("a","aa")
                .select("b","bb")
                .from("a")
                .where(true,SQL.BIND_AND,"fa",IWhere.COND_QIS,"va")
                .where(true,SQL.BIND_AND,null,IWhere.COND_LBRACKET,null)
                .where(true,SQL.BIND_AND,null,IWhere.COND_LBRACKET,null)
                .where(true,SQL.BIND_AND,null,IWhere.COND_LBRACKET,null)
                .where(true,SQL.BIND_AND,"fb",IWhere.COND_QIS,2)
                .where(true,SQL.BIND_OR,"fb",IWhere.COND_QIS,2)
                .where(true,SQL.BIND_AND,null,IWhere.COND_RBRACKET,null)
                .where(true,SQL.BIND_AND,"fb",IWhere.COND_QIS,2)
                .where(true,SQL.BIND_OR,"fb",IWhere.COND_QIS,3)
                .where(true,SQL.BIND_AND,null,IWhere.COND_RBRACKET,null)
                .where(true,SQL.BIND_AND,null,IWhere.COND_RBRACKET,null);
        logger.info(select.getSql());
        logger.info(select.getExecSql());
    }
}