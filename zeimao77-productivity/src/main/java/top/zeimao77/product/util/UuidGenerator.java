package top.zeimao77.product.util;

import java.util.UUID;

public class UuidGenerator implements IdGenerator<String>{

    public static final UuidGenerator INSTANCE = new UuidGenerator();

    @Override
    public String generate() {
        String s = UUID.randomUUID().toString().replaceAll("-", "");
        return s;
    }

}
