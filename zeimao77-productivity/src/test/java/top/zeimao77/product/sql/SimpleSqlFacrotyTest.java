package top.zeimao77.product.sql;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.main.BaseMain;

class SimpleSqlFacrotyTest extends BaseMain {

    @Test
    void execute() {
        SimpleSqlFacroty postgre_local_zeimao77 = ComponentFactory.initSimpleSqlFacroty("postgre_local_zeimao77");
        postgre_local_zeimao77.execute(o -> {
            o.update("update test set title = 'hello111' where id = '51789089363460097';");
            return null;
        });
    }
}