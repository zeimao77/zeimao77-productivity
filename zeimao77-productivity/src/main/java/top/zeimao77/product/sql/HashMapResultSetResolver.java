package top.zeimao77.product.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashMapResultSetResolver implements ResultSetResolver {

    private List<Map<String,Object>> list;

    public HashMapResultSetResolver(List<Map<String, Object>> list) {
        this.list = list;
    }

    @Override
    public void resolve(ResultSet rs, ResultSetMetaData rsmd) throws SQLException {
        int colCount = rsmd.getColumnCount();
        Map<String,Object> t = new HashMap<>();
        for(int i = 1;i<=colCount;i++){
            String columnLabel = rsmd.getColumnLabel(i);
            Object fieldValue = null;
            Object value = rs.getObject(i);
            if(value == null) {
                fieldValue = null;
            } else {
                int columnType = rsmd.getColumnType(i);
                switch (columnType) {
                    case Types.TIMESTAMP -> fieldValue = FiledTypeResover.LOCALDATETIMETYPERESOVER.resove(value);
                    case Types.DATE -> fieldValue = FiledTypeResover.LOCALDATETYPERESOVER.resove(value);
                    case Types.TIME -> fieldValue = FiledTypeResover.LOCALTIMETYPERESOVER.resove(value);
                    case Types.BINARY,Types.VARBINARY,Types.LONGVARBINARY -> fieldValue = FiledTypeResover.BYTEBUFFERTYPERESOVER.resove(value);
                    default -> fieldValue = rs.getObject(i);
                }
            }
            t.put(columnLabel,fieldValue);
        }
        list.add(t);
    }
}
