package top.zeimao77.product.security;

/**
 * 字符串脱敏工具
 */
public class DesensitizationUtil {

    /**
     * 从某一位脱敏到第几位,示例:
     * desensitization("13344556677,3,-4)  ==>> 133****6677
     * desensitization("test@foxmail.com",1,4)  ==>>  t***@foxmail.com
     * @param str 脱敏字符串
     * @param start 起始位置
     * @param end 结束位置
     * @return 脱敏后的结果
     */
    public static String desensitization(String str,int start,int end) {
        return desensitization(str,start,end,'*');
    }

    /**
     * @param str 脱敏字符串
     * @param start 起始位置
     * @param end 结束位置
     * @param c 替代字符
     * @return 脱敏后的结果
     */

    public static String desensitization(String str,int start,int end,char c) {
        if(str == null) {return null;}
        int index1 = start;
        int index2 = end > 0 ? end : str.length() + end;
        if(index2 < 0 || str.length() < index2) {return str;}
        String s1 = str.substring(0,index1);
        String s2 = str.substring(index2);
        StringBuilder sBuilder = new StringBuilder(s1);
        for (int i = index1; i < index2; i++) {
            sBuilder.append(c);
        }
        sBuilder.append(s2);
        return sBuilder.toString();
    }

    /**
     * 手机号脱敏
     * @param mobile 手机号
     * @return 脱敏后的结果
     */
    public static String mobilephone(String mobile) {
        return desensitization(mobile,3,-4);
    }

    /**
     * 姓名脱敏
     * @param name 姓名
     * @return 脱敏后的结果
     */
    public static String name(String name) {
        if(name == null) {return null;}
        if(name.length() == 2) {
            return desensitization(name,1,0);
        } else if(name.length() > 2) {
            return desensitization(name,1,-1);
        }
        return name;
    }

    /**
     * 邮箱脱敏
     * @param email 邮箱
     * @return 脱敏后的结果
     */
    public static String email(String email) {
        int i = email.indexOf('@');
        return desensitization(email,1,i);
    }

    /**
     * 固话脱敏
     * @param phone 坐机号
     * @return 脱敏后的结果
     */
    public static String telephone(String phone) {
        String[] split = phone.split("-");
        StringBuilder sBuiler = new StringBuilder(phone.length());
        for (int i = 0; i < split.length; i++) {
            sBuiler.append(desensitization(split[i],1,-1));
            if(i < split.length-1) {
                sBuiler.append("-");
            }
        }
        return sBuiler.toString();
    }

    /**
     * 身份证脱敏
     */
    public static String identityCard(String id) {
        return desensitization(id,6,14);
    }


}
