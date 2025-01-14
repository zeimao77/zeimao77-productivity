package top.zeimao77.product.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class SimpleBatchExecutorsService<T> {

    private static final Logger logger = LoggerFactory.getLogger(SimpleBatchExecutorsService.class);

    private ExecutorService executorService;

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void start(List<T> list, int pageSize) {
        int t = list.size() % pageSize == 0 ? list.size() / pageSize : list.size() / pageSize + 1;
        for (int i = 0; i < t; i++) {
            int s1 = i * pageSize;
            int e1 = (i + 1) * pageSize;
            e1 = e1 > list.size() ? list.size() : e1;
            int finalE = e1;
            executorService.submit(() -> {
                doHandler(list, s1, finalE);
            });
        }
    }

    public void over(long timeout, TimeUnit unit) {
        try {
            executorService.shutdown();
            this.executorService.awaitTermination(timeout, unit);
        } catch (InterruptedException e) {
            logger.error("线程中断退出阻塞", e);
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public abstract int doHandler(List<T> list, int start, int end);




}
