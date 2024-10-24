package top.zeimao77.product.sql;

import java.util.ArrayList;
import java.util.List;

public class DefaultFieldTypeResover {

    private List<FiledTypeResover> resovers;

    public static final DefaultFieldTypeResover DEFAULT = new DefaultFieldTypeResover(
        List.of(FiledTypeResover.STRINGTYPERESOVER,
            FiledTypeResover.LONGTYPERESOVER,
            FiledTypeResover.INTEGTYPERESOVER,
            FiledTypeResover.DOUBLETYPERESOVER,
            FiledTypeResover.BIGDECIMALTYPERESOVER,
            FiledTypeResover.BOOLEANTYPERESOVER,
            FiledTypeResover.DATETYPERESOVER,
            FiledTypeResover.LOCALDATETIMETYPERESOVER,
            FiledTypeResover.LOCALTIMETYPERESOVER,
            FiledTypeResover.LOCALDATETYPERESOVER,
            FiledTypeResover.FLOATTYPERESOVER,
            FiledTypeResover.SHORTTYPERESOVER,
            FiledTypeResover.CHARTYPERESOVER,
            FiledTypeResover.BIGINTTYPERESOVER,
            FiledTypeResover.BYTEBUFFERTYPERESOVER,
            FiledTypeResover.FIELDTYPERESOVER,
            FiledTypeResover.IJSONTYPERESOVER,
            FiledTypeResover.OBJECTTYPERESOVER
        )
    );

    public DefaultFieldTypeResover(List<FiledTypeResover> resovers) {
        this.resovers = resovers;
    }

    public DefaultFieldTypeResover() {
        this.resovers = new ArrayList<>(32);
    }

    public <T> T resolve(Object value, Class<T> clazz) {
        for (int i1 = 0; i1 < resovers.size(); i1++) {
            FiledTypeResover filedTypeResover = resovers.get(i1);
            if(filedTypeResover.support(clazz,value)) {
                T fieldValue = (T) filedTypeResover.resove(value);
                return fieldValue;
            }
        }
        return null;
    }

    public void addFieldTypeResover(FiledTypeResover filedTypeResover) {
        resovers.add(filedTypeResover);
    }

}
