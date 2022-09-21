package top.zeimao77.product.sql;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.main.BaseMain;

import java.time.LocalDateTime;

class SQLTest extends BaseMain {

    @Test
    void getExecSql() {
        SQL sql = new SQL()
                .select()
                .from("demo")
                .where(true,IWhere.BIND_AND,"create_date",IWhere.COND_QIS,"to_date(",",'yyyy-MM-dd HH24:mi:ss')", LocalDateTime.now());
        logger.info(sql.getExecSql());
    }
}

