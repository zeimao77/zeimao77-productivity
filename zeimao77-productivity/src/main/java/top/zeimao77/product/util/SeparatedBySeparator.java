package top.zeimao77.product.util;

import java.util.ArrayList;

public class SeparatedBySeparator {

    private char espectancy = 0x5C;  // 转义符 缺省值:'\'
    private char separator = 0x7C;  // 分隔符 缺省值:'|'
    private String patameter;
    private ArrayList<String> values = new ArrayList<>();

    public SeparatedBySeparator (String patameter) {
        this.patameter = patameter;
    }

    public SeparatedBySeparator(String patameter,char separator) {
        this.separator = separator;
        this.patameter = patameter;
    }

    public SeparatedBySeparator( String patameter, char separator, char espectancy) {
        this.espectancy = espectancy;
        this.separator = separator;
        this.patameter = patameter;
    }

    public void parse() {
        if(AssertUtil.isEmpty(this.patameter))
            return;
        StringBuilder curr = new StringBuilder();
        boolean escaped = false;
        for (int j = 0; j < this.patameter.length(); j++) {
            char c = this.patameter.charAt(j);
            if(escaped) {
                if(c == separator) {
                    curr.append(separator);
                    escaped = false;
                } else if(c == espectancy) {
                    curr.append(espectancy);
                } else {
                    curr.append(espectancy).append(c);
                    escaped = false;
                }
            } else if(c == espectancy) {
                escaped = true;
            } else if(c == separator) {
                this.values.add(curr.toString());
                curr.setLength(0);
            } else {
                curr.append(c);
            }
        }
        this.values.add(curr.toString());
    }

    public String getValue(int index) {
        if(this.values.isEmpty())
            this.parse();
        int i = index > 0 ? index - 1 : values.size() + index;
        return i >= values.size() || i < 0 ? null : values.get(i);
    }

    public ArrayList<String> getValues() {
        if(this.values.isEmpty())
            this.parse();
        return values;
    }
}
