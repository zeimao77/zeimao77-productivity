package top.zeimao77.product.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 一个仅将可执行SQL输出的SQL执行器
 */
public class OnlyPrintReposit implements  Reposit, Closeable {

    private static Logger logger = LogManager.getLogger(OnlyPrintReposit.class);

    PrintWriter writer;

    /**
     * @param writer SQL输出位置
     */
    public OnlyPrintReposit(PrintWriter writer){
        this.writer = writer;
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
    public int update(String sql) {
        logger.debug(sql);
        writer.println(sql);
        return 0;
    }

    @Override
    public int update(String sqlt, Object param) {
        DefaultStatementParamResolver defaultStatementParamResolver = new DefaultStatementParamResolver(sqlt, param);
        String execSql = defaultStatementParamResolver.getExecSql();
        logger.debug(execSql);
        writer.println(execSql);
        return 0;
    }

    @Override
    public <T> ArrayList<T> selectListObj(String sqlt, Object param, Class<T> clazz) {
        DefaultStatementParamResolver defaultStatementParamResolver = new DefaultStatementParamResolver(sqlt, param);
        String execSql = defaultStatementParamResolver.getExecSql();
        logger.debug(execSql);
        writer.println(execSql);
        return new ArrayList<>();
    }

    @Override
    public <T> ArrayList<T> selectListObj(String sql, Class<T> clazz) {
        logger.debug(sql);
        writer.println(sql);
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Map<String, Object>> selectListMap(String sqlt, Object param) {
        DefaultStatementParamResolver defaultStatementParamResolver = new DefaultStatementParamResolver(sqlt, param);
        String execSql = defaultStatementParamResolver.getExecSql();
        logger.debug(execSql);
        writer.println(execSql);
        return new ArrayList<>();
    }

    @Override
    public void close() {
        writer.flush();
        writer.close();
    }
}
