package com.zeimao77;
import top.zeimao77.product.factory.BeanFactory;
import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.main.BaseMain;

public class Main extends BaseMain {

    public static final String MYSQLBEAN = "mysql_top_zeimao77";

    public static void main(String[] args) {
        ComponentFactory.initSimpleSqlClient(MYSQLBEAN, BeanFactory.DEFAULT);

    }


}
