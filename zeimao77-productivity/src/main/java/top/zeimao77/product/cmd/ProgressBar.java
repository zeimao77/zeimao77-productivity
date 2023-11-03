package top.zeimao77.product.cmd;

import top.zeimao77.product.jobs.TokenBucket;

import java.util.concurrent.TimeUnit;

public class ProgressBar {

    private int cap;
    private int cur = 0;
    private static final int LENGTH = 50;
    private String title;
    private boolean cancel = false;

    public void start() {
        Thread thread = new Thread(() -> {
            TokenBucket.SleetStrategy sleetStrategy = new TokenBucket.SleetStrategy(1, TimeUnit.SECONDS);
            boolean e;
            do {
                e = cur < cap;
                System.out.print(bar());
                sleetStrategy.sleep();
            } while (e && !cancel);
            if(cancel) {
                System.out.println();
                System.out.println(title+":|cancel;");
            }
        });
        thread.start();
    }

    public void cancel() {
        this.cancel = true;
    }

    public ProgressBar(int cap) {
        this("Progress",cap);
    }

    public ProgressBar(String title,int cap) {
        this.title = title;
        this.cap = cap;
    }

    public int getCur() {
        return cur;
    }

    public void addCur(int a) {
        this.cur += a;
    }

    public void setCur(int cur) {
        this.cur = cur;
    }

    private String bar() {
        return bar(this.title,cur / (double)cap);
    }

    private static String bar(String title,double p) {
        int l = (int) (p * LENGTH);
        StringBuilder barBuilder = new StringBuilder((LENGTH + title.length() + 7) * 2);
        for (int i = 0; i < (LENGTH + title.length() + 7); i++) {
            barBuilder.append("\b");
        }
        barBuilder.append(title).append(":|");
        for (int i = 0; i < l; i++)
            barBuilder.append("█");
        l = LENGTH - l;
        for (int i = 0; i < l; i++)
            barBuilder.append("░");
        barBuilder.append("|");
        barBuilder.append(String.format("%3.0f%%",p * 100));
        return barBuilder.toString();
    }

}
