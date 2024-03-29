package top.zeimao77.product.cmd;

import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.factory.BeanFactory;
import top.zeimao77.product.factory.ComponentFactory;

import java.util.Scanner;

public class TokenAuthentication {

    public String token;

    public TokenAuthentication(String token) {
        this.token = token;
    }

    public Integer authentication(Integer maxTry) {
        for (int i = 0; i < maxTry; i++) {
            System.out.println(i == 0 ? "请输入口令:" : String.format("口令错误,请输入口令[%d/%d]:",i,maxTry));
            String s = BeanFactory.DEFAULT.getBean(ComponentFactory.AUTOBEAN_SYSINSCANNER,Scanner.class).nextLine();
            if(token.equals(s)) {
                return 1;
            }
        }
        throw new BaseServiceRunException(BaseServiceRunException.NO_PERMISSION,"没有权限");
    }

    public String getToken() {
        return token;
    }

}
