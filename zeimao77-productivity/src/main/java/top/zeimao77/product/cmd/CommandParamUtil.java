package top.zeimao77.product.cmd;

import top.zeimao77.product.util.BoolUtil;
import top.zeimao77.product.util.StringOptional;

import java.util.Scanner;

public class CommandParamUtil {

    private CommandParamUtil(){}

    public static StringOptional getParamFromSystemIn(String msg){
        System.out.print(msg);
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        scanner.close();
        return new StringOptional(s);
    }

    public static StringOptional normalParam(String[] args, int position) {
        if(args.length >= position || position < 0) {
            return new StringOptional(args[position]);
        }
        return StringOptional.empty();
    }

    public static StringOptional normalParam(String[] args, int position,String msg) {
        if(args != null && position >= 0 && position < args.length)
            return new StringOptional(args[position]);
        return getParamFromSystemIn(msg+",请输入:");
    }

    public static Boolean booleanParam(String[] args,String key) {
        for (String arg : args) {
            if(key.equals(arg)) return true;
        }
        return false;
    }

    public static Boolean booleanParam(String[] args,String key,String msg) {
        if(args != null) {
            for (String arg : args) {
                if(key.equals(arg)) return true;
            }
        }
        String s = getParamFromSystemIn(msg+",请输入[Y|N]:").get();
        return BoolUtil.parseBool(s);
    }

    public static StringOptional valueParam(String[] args,String key) {
        int vIndex = Integer.MAX_VALUE;
        for (int i = 0; i < args.length; i++) {
            if(key.equals(args[i])) {
                vIndex = i+1;
                break;
            }
        }
        if(vIndex >= args.length)
            return StringOptional.empty();
        return new StringOptional(args[vIndex]);
    }


    public static StringOptional valueParam(String[] args,String key,String msg) {
        int vIndex = Integer.MAX_VALUE;
        if(args != null) {
            for (int i = 0; i < args.length; i++) {
                if(key.equals(args[i])) {
                    vIndex = i+1;
                    break;
                }
            }
        }
        if(args != null && vIndex >= args.length) {
            return getParamFromSystemIn(msg+",请输入:");
        }
        return new StringOptional(args[vIndex]);
    }


}
