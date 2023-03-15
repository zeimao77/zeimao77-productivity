package top.zeimao77.product.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static Logger logger = LoggerFactory.getLogger(OnlyPrintReposit.class);

    private boolean appendEnd = true;
    PrintWriter writer;

    /**
     * @param writer SQL输出位置
     */
    public OnlyPrintReposit(PrintWriter writer){
        this.writer = writer;
    }

    public OnlyPrintReposit(PrintWriter writer,boolean appendEnd) {
        this.appendEnd = appendEnd;
        this.writer = writer;
    }

    private void printByResolver(StatementParamResolver sql) {
        sql.resolve();
        String execSql = sql.getExecSql();
        logger.debug(execSql);
        if(writer != null) {
            writer.print(execSql);
            if(appendEnd)
                writer.println(";");
            else
                writer.println();
        }
    }

    @Override
    public <T> ArrayList<T> selectByResolver(StatementParamResolver sql, Class<T> clazz) {
        printByResolver(sql);
        return new ArrayList<>();
    }

    @Override
    public int updateByResolver(StatementParamResolver sql) {
        printByResolver(sql);
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
        if(writer != null) {
            writer.print(sql);
            if(appendEnd)
                writer.println(";");
            else
                writer.println();
        }
        return 0;
    }

    @Override
    public int update(String sqlt, Object param) {
        return updateByResolver(new DefaultStatementParamResolver(sqlt, param));
    }

    @Override
    public <T> ArrayList<T> selectListObj(String sqlt, Object param, Class<T> clazz) {
        selectByResolver(new DefaultStatementParamResolver(sqlt, param),clazz);
        return new ArrayList<>();
    }

    @Override
    public <T> ArrayList<T> selectListObj(String sql, Class<T> clazz) {
        logger.debug(sql);
        if(writer != null) {
            writer.print(sql);
            if(appendEnd)
                writer.println(";");
            else
                writer.println();
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Map<String, Object>> selectListMap(String sqlt, Object param) {
        printByResolver(new DefaultStatementParamResolver(sqlt, param));
        return new ArrayList<>();
    }

    @Override
    public void close() {
        if(writer != null) {
            writer.flush();
            writer.close();
        }
    }

    public boolean isAppendEnd() {
        return appendEnd;
    }

    public void setAppendEnd(boolean appendEnd) {
        this.appendEnd = appendEnd;
    }
}
