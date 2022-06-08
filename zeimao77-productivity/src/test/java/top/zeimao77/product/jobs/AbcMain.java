package top.zeimao77.product.jobs;

import top.zeimao77.product.main.BaseMain;

public class AbcMain extends BaseMain {

    public static void main(String[] args) {
        BaseMain.showBanner();
        AbcInsertJob job = new AbcInsertJob(10010);
        job.prepare();
        job.start(2,300);
    }

}
