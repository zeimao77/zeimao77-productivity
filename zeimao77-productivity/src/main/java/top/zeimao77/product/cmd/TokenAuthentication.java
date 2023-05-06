package top.zeimao77.product.cmd;

import java.util.Scanner;

public class TokenAuthentication {

    public String token;

    public TokenAuthentication(String token) {
        this.token = token;
    }

    public Integer authentication(Integer maxTry) {
        Integer result = 0;
        try(Scanner scanner = new Scanner(System.in)) {
            for (int i = 0; i < maxTry; i++) {
                System.out.println(i == 0 ? "请输入口令:" : "口令错误,请输入口令:");
                String s = scanner.nextLine();
                if(token.equals(s)) {
                    result = 1;
                    break;
                }
            }
        }
        return result;
    }

    public String getToken() {
        return token;
    }

}
