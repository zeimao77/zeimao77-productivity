package top.zeimao77.product.model;

public interface Orderd extends Comparable<Orderd>{

    int orderd();

    @Override
    default int compareTo(Orderd o) {
        return Integer.compare(this.orderd(),o.orderd());
    }

}
