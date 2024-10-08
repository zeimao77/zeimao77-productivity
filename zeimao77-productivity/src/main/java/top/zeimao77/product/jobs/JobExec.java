package top.zeimao77.product.jobs;
import top.zeimao77.product.util.LongBitMap;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.APPERR;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.RETRYABLE;

public interface JobExec {

    int SUCCESSED = 1,FAILED = APPERR;

    class Result {

        public static final Result SUCCESS = new Result(SUCCESSED,"OK",null);
        public static final Result fail(Integer code,String msg,Object data) {
            Result result = new Result(code,msg,data);
            return result;
        }

        public static final Result success(Object data) {
            return new Result(SUCCESSED,"OK",data);
        }

        private Integer resultCode;
        private String resultMsg;
        private Object data;

        public boolean success() {
            return this.resultCode == SUCCESSED;
        }

        public boolean retrieable(){
            return !LongBitMap.matches(this.resultCode,RETRYABLE);
        }

        /**
         * @param resultCode 响应码
         * @param resultMsg 响应消息
         * @param data 携带一个数据
         */
        public Result(Integer resultCode, String resultMsg,Object data) {
            this.resultCode = resultCode;
            this.resultMsg = resultMsg;
        }

        public Integer getResultCode() {
            return resultCode;
        }

        public void setResultCode(Integer resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

}
