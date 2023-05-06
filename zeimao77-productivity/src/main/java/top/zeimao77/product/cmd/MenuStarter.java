package top.zeimao77.product.cmd;

public interface MenuStarter {

    String name();

    /**
     * 返回1表示鉴权通过;
     * @return
     */
    default Integer authentication() { return 1; }

    default void start(String[] args) {
        this.authentication();
        this.doStart(args);
    }

    void doStart(String[] args);

}
