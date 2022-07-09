package top.zeimao77.product.main;

import top.zeimao77.product.fileio.TextFileUtil;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkMain extends BaseMain{

    static Pattern pattern = Pattern.compile("\\s+public void (\\S+)\\(.*");


    public static void main(String[] args) {
        File file = new File("E:\\shijicode\\app-main\\src\\main\\java\\com\\zeimao77\\model\\ZtOrderHead.java");
        TextFileUtil.lineConsumer(file,(i,l) -> {
            Matcher matcher = pattern.matcher(l);
            if(matcher.matches()) {
                String group = matcher.group(1);
                System.out.println("        head."+group+"(null);");
            }
        });
    }


}
