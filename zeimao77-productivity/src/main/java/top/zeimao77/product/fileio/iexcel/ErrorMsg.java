package top.zeimao77.product.fileio.iexcel;

import top.zeimao77.product.exception.ExceptionCodeDefinition;

public class ErrorMsg {

    private int rowNo;
    private Table.Column column;
    private Integer errorCode;
    private String errorMsg;
    private Throwable throwable;

    public ErrorMsg(int rowNo, Table.Column column, String errorMsg) {
        this.rowNo = rowNo;
        this.column = column;
        this.errorCode = ExceptionCodeDefinition.UNKNOWN;
        this.errorMsg = errorMsg;
    }

    public ErrorMsg(int rowNo, Table.Column column, Integer errorCode, String errorMsg) {
        this.rowNo = rowNo;
        this.column = column;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ErrorMsg(int rowNo, Table.Column column, Integer errorCode, String errorMsg, Throwable throwable) {
        this.rowNo = rowNo;
        this.column = column;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.throwable = throwable;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public Table.Column getColumn() {
        return column;
    }

    public void setColumn(Table.Column column) {
        this.column = column;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String describe() {
        return String.format("第%d行第%d列[%s]数据非法:[%d]%s",this.rowNo,column.getIndex()+1,column.getField(),this.errorCode,this.errorMsg);
    }
}
