package top.zeimao77.product.cmd;

import top.zeimao77.product.model.ImmutablePair;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 主要是遵循linux的命令行参数解析规则
 * @since 2.2.2
 */
public class CmdArgUtil {

    /**
     * 短格式的参数解析
     * @param args 原始参数列表
     * @param optString 参数选项 如果取值(非标记选项)在字母后加上:
     * @return 解析后的参数列表
     */
    public static ArrayList<ImmutablePair<Character,String>> getOpts(String[] args, String optString) {
        HashMap<Character,Boolean> optsType = new HashMap<>();
        ArrayList<ImmutablePair<Character,String>> result = new ArrayList<>();
        for (int i = 0; i < optString.length(); i++) {
            if(optString.charAt(i+1) == 0x3A) {
                optsType.put(optString.charAt(i),true);
                i++;
            } else {
                optsType.put(optString.charAt(i),false);
            }
        }
        for (int i = 0; i < args.length; i++) {
            if(args[i].charAt(0) == 0x2D && optsType.containsKey(args[i].charAt(1))) {
                if(args[i].length() == 2 && !optsType.get(args[i].charAt(1))) {
                    result.add(new ImmutablePair<>(args[i].charAt(1),null));
                } else if(args[i].length() == 2 && optsType.get(args[i].charAt(1))) {
                    result.add(new ImmutablePair<>(args[i].charAt(1),args[i+1]));
                    i++;
                } else if(args[i].length() > 2 && optsType.get(args[i].charAt(1))) {
                    result.add(new ImmutablePair<>(args[i].charAt(1),args[i].substring(2)));
                }
            }
        }
        return result;
    }

    public static boolean isHelp(String[] args) {
        return args[args.length-1] == "-h" || args[args.length-1] == "--help";
    }

    /**
     *
     * @param args 原始参数列表
     * @param opt 短格式参数 参数不要加-
     * @param optLong 长格式参数 参数不要加--
     * @param hasVal 是否需要值
     * @return 如果需要值则返回值 否则返回选项格式
     */
    public static String findOpt(String[] args, String opt, String optLong, boolean hasVal) {
        for (int i = 0; i < args.length; i++) {
            if(opt != null && args[i].equals("-"+ opt))
                return hasVal ? args[i+1] : opt;
            if(optLong != null && args[i].equals("--" + optLong))
                return hasVal ? args[i+1] : optLong;
            if(optLong != null && args[i].startsWith("--"+optLong+"=") && args[i].length() > optLong.length() + 2)
                return hasVal ? args[i].substring(optLong.length() + 3) : optLong;
        }
        return null;
    }

}
