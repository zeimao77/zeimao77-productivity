package com.zeimao77;
import top.zeimao77.product.http.HttpClientUtil8;
import top.zeimao77.product.main.BaseMain;

import java.io.UnsupportedEncodingException;
public class Main extends BaseMain {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String s = new HttpClientUtil8().sendGet("https://www.zeimao77.top/stadir/66", null, 1000);
        System.out.println(s);
    }


}
