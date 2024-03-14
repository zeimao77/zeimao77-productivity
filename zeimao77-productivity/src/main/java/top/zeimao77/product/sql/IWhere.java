package top.zeimao77.product.sql;

public interface IWhere {

    String BIND_AND = "AND";
    String BIND_OR = "OR";
    String COND_QIN = "$in";
    String COND_QNIN = "$nin";
    String COND_QIS = "$is";
    String COND_QNE = "$ne";
    String COND_QLIKE = "$like";
    String COND_QLLIKE = "$llike";  // 左边添加百分号
    String COND_QRLIKE = "$rlike";  // 右边添加百分号
    String COND_QGT = "$gt";
    String COND_QGTE = "$gte";
    String COND_QLT = "$lt";
    String COND_QLTE = "$lte";
    String COND_QREGEXP = "$regexp";
    String COND_LBRACKET = "$lbrkt";
    String COND_RBRACKET = "$rbrkt";

}
