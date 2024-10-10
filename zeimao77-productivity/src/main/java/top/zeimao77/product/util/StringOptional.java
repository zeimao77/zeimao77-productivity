package top.zeimao77.product.util;

import top.zeimao77.product.exception.BaseServiceRunException;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.WRONG_SOURCE;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class StringOptional {

    public static final StringOptional EMPTY = new StringOptional(null);

    private final Optional<String> optional;

    public StringOptional(String value){
        this.optional = Optional.ofNullable(value);
    }

    public static StringOptional trimSpacesOf(String value) {
        return new StringOptional(StringUtil.trimSpaces(value));
    }

    public static StringOptional empty() {
        return EMPTY;
    }

    public boolean test(Predicate<Optional<String>> predicate) {
        return predicate.test(optional);
    }

    public void ifTestFalse(Predicate<Optional<String>> predicate, Consumer<String> action) {
        if (!predicate.test(optional))
            action.accept(optional.get());
    }

    public boolean isBlank() {
        return optional.filter(o -> !o.trim().isEmpty()).isEmpty();
    }

    public void ifNotBlank(Consumer<String> action) {
        if (!isBlank())
            action.accept(optional.get());
    }


    public String orBlankGet(String defaultValue) {
        return isBlank() ? defaultValue : optional.get();
    }

    public String orBlankGet(Supplier<String> supplier) {
        return isBlank() ? supplier.get() : optional.get();
    }

    public String ifBlankThrow(String fieldName) {
        if(isBlank())
            throw new BaseServiceRunException(WRONG_SOURCE,fieldName + "参数必需;");
        return optional.get();
    }

    public <X extends Throwable> String ifBlankThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isBlank())
            return optional.get();
        else
            throw exceptionSupplier.get();
    }

    public boolean isEmpty() {
        return optional.filter(o -> !o.isEmpty()).isEmpty();
    }

    public void ifNotEmpty(Consumer<String> action) {
        if (!isEmpty())
            action.accept(optional.get());
    }

    public String orEmptyGet(String defaultValue) {
        return isEmpty() ? defaultValue : optional.get();
    }

    public String orEmptyGet(Supplier<String> supplier) {
        return isEmpty() ? supplier.get() : optional.get();
    }

    public String ifEmptyThrow(String fieldName) {
        if(isEmpty())
            throw new BaseServiceRunException(WRONG_SOURCE,fieldName + "参数必需;");
        return optional.get();
    }

    public <X extends Throwable> String ifEmptyThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isEmpty())
            return optional.get();
        else
            throw exceptionSupplier.get();
    }

    public boolean isNull() {
        return optional.isEmpty();
    }

    public void ifNotNull(Consumer<String> action) {
        if (!isNull())
            action.accept(optional.get());
    }

    public String orNullGet(String defaultValue) {
        return isNull() ? defaultValue : optional.get();
    }

    public String orNullGet(Supplier<String> supplier) {
        return isNull() ? supplier.get() : optional.get();
    }

    public String ifNullThrow(String fieldName) {
        if(isNull())
            throw new BaseServiceRunException(WRONG_SOURCE,fieldName + "参数必需;");
        return optional.get();
    }

    public <X extends Throwable> String ifNullThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isNull())
            return optional.get();
        else
            throw exceptionSupplier.get();
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
