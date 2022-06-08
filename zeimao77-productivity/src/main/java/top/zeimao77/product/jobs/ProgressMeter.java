package top.zeimao77.product.jobs;

public class ProgressMeter {

    private int total;
    private int current;

    public ProgressMeter(int total) {
        this(total,0);
    }

    public ProgressMeter(int total,int current) {
        this.total = total;
        this.current = current;
    }

    public int getTotal() {
        return this.total / 100;
    }

    public int current() {
        return current;
    }

    public synchronized int add() {
        return current++;
    }

    public synchronized int add(int i) {
        return current += i;
    }

    public int progress() {
        int progress = current * 100 / total;
        return progress;
    }

}
