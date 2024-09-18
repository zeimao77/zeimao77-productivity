package com.zeimao77;
import top.zeimao77.product.main.BaseMain;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class Main extends BaseMain {

    public static void main(String[] args) throws UnsupportedEncodingException {
        test();
    }

    public static HashMap<String,Object> test() {
        HashMap<String, Object> res = new HashMap<>();
        int r = 1;
        label1: {
            label2 : {
                if(r == 1) {
                    res.put("msg","FAIL1");
                    break label1;
                }
                //todo
                res.put("msg","OK");
            }
            res.put("msg","label2");
        }
        // TODO
        System.out.println(res.get("msg"));
        return res;
    }


}
