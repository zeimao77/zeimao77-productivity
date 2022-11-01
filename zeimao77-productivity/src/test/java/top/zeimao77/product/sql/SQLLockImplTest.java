package top.zeimao77.product.sql;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.UuidGenerator;

class SQLLockImplTest extends BaseMain {

    private static final String MYSQL="mysql_top_zeimao77";

    SimpleSqlTemplate sqlTemplate = ComponentFactory.initSimpleSqlTemplate(MYSQL,null);


    @Test
    void lock() {
        SimpleSqlClient client = sqlTemplate.createClient();
        SQLLockImpl sqlLock = new SQLLockImpl(client);
        String lockId = UuidGenerator.INSTANCE.generate();
        boolean lock = sqlLock.lock(lockId, lockId, 3, 100, 3000);
        logger.info("加锁结果:{}",lock);
        lock = sqlLock.lock(lockId, lockId, 3, 200, 1000);
        logger.info("加锁结果:{}",lock);
        lock = sqlLock.unLock(lockId, lockId);
        logger.info("解锁结果:{}",lock);

    }
}