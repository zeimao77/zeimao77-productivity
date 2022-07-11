package top.zeimao77.product.main;

import top.zeimao77.product.fileio.TextFileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkMain extends BaseMain{

    static Pattern pattern = Pattern.compile("\\s+private \\S+ (\\S+);.*");


    public static void main(String[] args) {
        File file = new File("E:\\shijicode\\app-main\\src\\main\\java\\com\\zeimao77\\model\\ZtOrderPayment.java");
        ArrayList<String> s = new ArrayList<>();
        TextFileUtil.lineConsumer(file,(i,l) -> {
            Matcher matcher = pattern.matcher(l);
            if(matcher.matches()) {
                String group = matcher.group(1);
                s.add(group);
            }
        });
        logger.info("SIZE:{}",s.size());
        for (String s1 : s) {
            System.out.print(","+s1.toUpperCase() + " AS \"" + s1 + "\"");
        }
        System.out.println();
        for (String s1 : s) {
            System.out.print(","+s1.toUpperCase());
        }
        System.out.println();
        for (String s1 : s) {
            System.out.print(",#{"+s1 + "}");
        }

    }


}
