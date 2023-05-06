package top.zeimao77.product.util;

import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.exception.ExceptionCodeDefinition;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class StringOptional {

    public static final Predicate<String> TRIM_BLACK_CHECK = o -> !o.trim().isEmpty() ;
    public static final StringOptional EMPTY = new StringOptional(null);

    private Predicate<String> check;
    private final Optional<String> optional;

    public StringOptional(String value){
        this.optional = Optional.ofNullable(value);
        this.check = TRIM_BLACK_CHECK;
    }

    public void setCheck(Predicate<String> check) {
        this.check = check;
    }

    public static StringOptional empty() {
        return EMPTY;
    }

    public boolean isBlack() {
        return optional.filter(check).isEmpty();
    }

    public void ifNotBlack(Consumer<String> action) {
        if (!isBlack())
            action.accept(optional.get());
    }

    public String orBlackGet(String defaultValue) {
        return isBlack() ? defaultValue : optional.get();
    }

    public String orBlackGet(Supplier<String> supplier) {
        return isBlack() ? supplier.get() : optional.get();
    }

    public String ifBlackThrow(String fieldName) {
        if(isBlack())
            throw new BaseServiceRunException(ExceptionCodeDefinition.WRONG_SOURCE,fieldName + "参数必需;");
        return optional.get();
    }

    public <X extends Throwable> String ifBlackThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isBlack()) {
            return optional.get();
        } else {
            throw exceptionSupplier.get();
        }
    }

    public Optional<String> getOptional() {
        return optional;
    }

    public String get() {
        return optional.get();
    }

    public Integer getInteger() {
        if(optional.get().startsWith("0x")) {
            Long.valueOf(optional.get().substring(2),16);
        }
        return Integer.valueOf(optional.get());
    }

    public boolean getBool() {
        return BoolUtil.parseBool(optional.get());
    }

    public Double getDouble() {
        return Double.parseDouble(optional.get());
    }

    public Long getLong() {
        if(optional.get().startsWith("0x")) {
            Long.valueOf(optional.get().substring(2),16);
        }
        return Long.valueOf(optional.get());
    }

    @Override
    public String toString() {
        return optional.isEmpty() ? null : optional.get();
    }
}
