package top.zeimao77.product.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.exception.ExceptionCodeDefinition;
import top.zeimao77.product.util.LocalDateTimeUtil;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author zeimao77
 * @since 2.1.4
 *
 * 使用该锁需要注意：
 * 1. 如果在不同服务器 服务器的时间同步问题
 * 2.
 *
 * CREATE TABLE LOCKIMPL (
 * lockId VARCHAR(64) NOT NULL COMMENT '锁ID',
 * lockValue VARCHAR(256) COMMENT '锁钥',
 * expiredTime DATETIME NOT NULL COMMENT '过期时间',
 * createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 * PRIMARY KEY (lockId),
 * KEY IDX_EXPIREDTIME(expiredTime)
 * )ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
 */
public class SQLLockImpl {

    private static Logger logger = LogManager.getLogger(SQLLockImpl.class);

    Reposit reposit;

    public SQLLockImpl(Reposit reposit){
        this.reposit = reposit;
    }

    public void refreshDb() {
        String sql = "DELETE FROM LOCKIMPL WHERE expiredTime < ?";
        this.reposit.update(sql,new Object[]{LocalDateTimeUtil.nowDateTime()});
    }

    public boolean tryLock(String lockId,String value,int expire) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plusSeconds(expire);
        try {
            int i = this.reposit.updateByResolver(new SQL()
                    .insert("LOCKIMPL")
                    .addValues("lockId", lockId)
                    .addValues(value != null,"lockValue", value)
                    .addValues("expiredTime", expireTime)
                    .addValues("createdTime", now)
                    .endValues()
            );
            return i > 0;
        }catch (BaseServiceRunException e) {
            if(e.matcheOriginError(ExceptionCodeDefinition.SQLICVEXCEPTION)) {
                logger.debug("锁({})已加锁",lockId);
            } else {
                logger.error("加锁错误",e);
            }
        }catch (Exception e) {
            logger.error("加锁错误",e);
        }
        return false;
    }

    public boolean lock(String lockId,String value,int expire,int waitTimeOut) {
        return lock(lockId,value,expire,100,waitTimeOut);
    }

    /**
     * 加锁
     * @param lockId 锁KEY
     * @param value 锁钥
     * @param expire 过期时间 单位:秒
     * @param sleep 尝试周期 单位：毫秒
     * @param waitTimeOut 等待时间 单位：毫秒
     * @return 是否加锁成功
     */
    public boolean lock(String lockId,String value,int expire,int sleep,int waitTimeOut) {
        int wait = 0;
        refreshDb();
        while (wait < waitTimeOut) {
            if(tryLock(lockId,value,expire)) {
                return true;
            }
            try {
                TimeUnit.MICROSECONDS.sleep(sleep);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            wait += sleep;
        }
        return false;
    }

    public boolean reLook(String lockId,String value,int expire) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plusSeconds(expire);
        try {
            refreshDb();
            int i = this.reposit.updateByResolver(new SQL()
                    .update("LOCKIMPL")
                    .set("expiredTime", expireTime)
                    .where(IWhere.BIND_AND, "lockId", IWhere.COND_QIS, lockId)
                    .where(value != null, IWhere.BIND_AND, "lockValue", IWhere.COND_QIS, value)
            );
            return i > 0;
        }catch (Exception e) {
            logger.warn("SQL重锁错误;",e);
        }
        return false;
    }

    public boolean unLock(String lockId) {
        String sql = "DELETE FROM LOCKIMPL WHERE lockId = ?";
        try {
            refreshDb();
            this.reposit.update(sql, new Object[]{lockId});
            return true;
        } catch (RuntimeException e) {
            logger.warn("SQL解锁错误;",e);
        }
        return false;
    }

    public boolean unLock(String lockId,String value) {
        String sql = "DELETE FROM LOCKIMPL WHERE lockId = ? AND lockValue = ?";
        try {
            refreshDb();
            this.reposit.update(sql, new Object[]{lockId,value});
            return true;
        } catch (RuntimeException e) {
            logger.warn("SQL解锁错误;",e);
        }
        return false;
    }

}
