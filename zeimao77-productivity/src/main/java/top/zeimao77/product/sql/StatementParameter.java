package top.zeimao77.product.sql;

import top.zeimao77.product.util.JsonBeanUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.BiPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatementParameter<T> {

    /**
     * jdbc参数类型 默认不知道类型
     */
    public static final int DEFAULT_JDBCTYPE = 0x7FFFFFFF;
    private int index,jdbcType = DEFAULT_JDBCTYPE;
    private String name;
    /**
     * 1 入参 IN
     * 2 出参 OUT
     */
    private int mode = 1;
    private Class<T> javaType;

    private String valSetPre;
    private String valSetPost;
    private T value;

    public String getValSetPre() {
        return valSetPre;
    }

    public void setValSetPre(String valSetPre) {
        this.valSetPre = valSetPre;
    }

    public String getValSetPost() {
        return valSetPost;
    }

    public void setValSetPost(String valSetPost) {
        this.valSetPost = valSetPost;
    }

    public StatementParameter(){}

    public StatementParameter(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(int jdbcType) {
        this.jdbcType = jdbcType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Class<T> getJavaType() {
        return javaType;
    }

    public void setJavaType(Class<T> javaType) {
        this.javaType = javaType;
    }

    public String str() {
        return JsonBeanUtil.DEFAULT.toJsonString(this);
    }

    public static class Builder {

        private int index,jdbcType = DEFAULT_JDBCTYPE,mode = 1;
        private String name;
        private Class<?> javaType;

        private String valSetPre,valSetPost;

        public static final BiPredicate<String,Class<?>> pre = (o1,o2) -> {
            if(o1.equals(o2.getSimpleName()) || o1.equals(o2.getName())) {
                return true;
            }
            return false;
        };

        private Builder(){}

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(int index,String name) {
            Builder builder = new Builder();
            builder.index = index;
            builder.name = name;
            return builder;
        }

        /**
         * str示例： “columnName,mode=IN,javaType=String,jdbcType=VARCHAR”
         * 默认为入参 MODE=OUT表示为出参，主要用于存储过程
         * javaType 入参和出参时需要指定
         * jdbcType 出参需要指定
         * @param str 字段串描述
         * @return 构建器
         */
        public static Builder create(String str) {
            Builder builder = new Builder();
            String[] split = str.split(",");
            Pattern pattern = Pattern.compile("(\\w+)=(.*)");
            for (String s : split) {
                Matcher matcher = pattern.matcher(s);
                if (matcher.matches()) {
                    String group = matcher.group(1);
                    if("NAME".equalsIgnoreCase(group)) {
                        builder.name = matcher.group(2);
                    } else if("MODE".equalsIgnoreCase(group)) {
                        builder.mode(matcher.group(2));
                    } else if("JDBCTYPE".equalsIgnoreCase(group)) {
                        builder.jdbcType(matcher.group(2));
                    } else if("JAVATYPE".equalsIgnoreCase(group)) {
                        builder.javaType(matcher.group(2));
                    }
                } else {
                    builder.name = s;
                }
            }
            return builder;
        }

        public Builder index(int index) {
            this.index = index;
            return this;
        }

        public Builder javaType(Class<?> javaType) {
            this.javaType = javaType;
            return this;
        }

        public Builder valSetPre(String valSetPre) {
            this.valSetPre = valSetPre;
            return this;
        }

        public Builder valSetPost(String valSetPost) {
            this.valSetPost = valSetPost;
            return this;
        }

        public Builder javaType(String javaType) {
            Class<?>[] javaTypes = new Class[] {
                    String.class
                    , Integer.class
                    , Double.class
                    , Long.class
                    , LocalDateTime.class
                    , LocalDate.class
                    , LocalTime.class
                    , BigDecimal.class
                    , Short.class
                    , Float.class
                    , BigInteger.class
                    , java.util.Date.class
                    , Boolean.class
                    , Character.class
                    , ByteBuffer.class
                    , Object.class
            };
            for (Class<?> type : javaTypes) {
                if(pre.test(javaType,type)) {
                    this.javaType = type;
                    break;
                }
            }
            return this;
        }

        public Builder jdbcType(String jdbcType) {
            switch (jdbcType) {
                case "VARCHAR":
                    this.jdbcType = Types.VARCHAR;
                    break;
                case "TINYINT" :
                    this.jdbcType = Types.TINYINT;
                    break;
                case "BIT" :
                    this.jdbcType = Types.BIT;
                    break;
                case "SMALLINT" :
                    this.jdbcType = Types.SMALLINT;
                    break;
                case "INTEGER":
                case "INT" :
                    this.jdbcType = Types.INTEGER;
                    break;
                case "BIGINT" :
                    this.jdbcType = Types.BIGINT;
                    break;
                case "FLOAT" :
                    this.jdbcType = Types.FLOAT;
                    break;
                case "DOUBLE" :
                    this.jdbcType = Types.DOUBLE;
                    break;
                case "DECIMAL" :
                    this.jdbcType = Types.DECIMAL;
                    break;
                case "CHAR" :
                    this.jdbcType = Types.CHAR;
                    break;
                case "LONGVARCHAR" :
                    this.jdbcType = Types.LONGVARCHAR;
                    break;
                case "DATE" :
                    this.jdbcType = Types.DATE;
                    break;
                case "TIME" :
                    this.jdbcType = Types.TIME;
                    break;
                case "TIMESTAMP" :
                    this.jdbcType = Types.TIMESTAMP;
                    break;
                case "NULL" :
                    this.jdbcType = Types.NULL;
                    break;
                default :
                    this.jdbcType = DEFAULT_JDBCTYPE;
                    break;
            }
            return this;
        }

        public Builder mode(String mode) {
            if("IN".equalsIgnoreCase(mode)) {
                this.mode = 1;
            } else if("OUT".equalsIgnoreCase(mode)) {
                this.mode = 2;
            }
            return this;
        }

        public StatementParameter build() {
            StatementParameter procedureParam = new StatementParameter(index,name);
            procedureParam.setMode(mode);
            procedureParam.setJavaType(javaType);
            procedureParam.setJdbcType(jdbcType);
            procedureParam.setValSetPre(valSetPre);
            procedureParam.setValSetPost(valSetPost);
            return procedureParam;
        }

    }
}
