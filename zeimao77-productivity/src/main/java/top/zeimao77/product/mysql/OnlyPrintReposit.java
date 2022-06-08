package top.zeimao77.product.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * 一个仅将可执行SQL输出的SQL执行器
 */
public class OnlyPrintReposit implements Reposit, Closeable {

    private static Logger logger = LogManager.getLogger(OnlyPrintReposit.class);

    PrintWriter writer;

    /**
     * @param writer SQL输出位置
     */
    public OnlyPrintReposit(PrintWriter writer){
        this.writer = writer;
    }

    @Override
    public boolean testConnection() {
        return true;
    }

    @Override
    public <T> ArrayList<T> selectByResolver(StatementParamResolver sql, Class<T> clazz) {
        String execSql = sql.getExecSql();
        logger.debug(execSql);
        writer.println(execSql);
        return new ArrayList<>();
    }

    @Override
    public int updateByResolver(StatementParamResolver sql) {
        String execSql = sql.getExecSql();
        logger.debug(execSql);
        writer.println(execSql);
        return 0;
    }

    @Override
    public <Z> int batchUpdate(List<Z> list, BiConsumer<SQL, Z> con) {
        for (Z z : list) {
            SQL sql = new SQL();
            con.accept(sql,z);
            updateByResolver(sql);
        }
        return 0;
    }


    @Override
    public void close() {
        writer.flush();
        writer.close();
    }
}
