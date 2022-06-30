package top.zeimao77.product.jobs;
import top.zeimao77.product.util.LongBitMap;

import static top.zeimao77.product.exception.ExceptionCodeDefinition.NON_RETRYABLE;

public interface JobExec {

    int SUCCESSED = 1,FAILED = 2;

    class Result {

        public static final Result SUCCESS = new Result(SUCCESSED,"OK");
        public static final Result fail(Integer code,String msg) {
            Result result = new Result(code,msg);
            return result;
        }

        private Integer resultCode;
        private String resultMsg;

        public boolean retrieable(){
            return !LongBitMap.matches(this.resultCode,NON_RETRYABLE);
        }

        public Result(Integer resultCode, String resultMsg) {
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
    }

}
