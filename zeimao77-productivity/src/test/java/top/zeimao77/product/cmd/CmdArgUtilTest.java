package top.zeimao77.product.cmd;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.main.BaseMain;

import static org.junit.jupiter.api.Assertions.*;

class CmdArgUtilTest extends BaseMain {

    @Test
    void getOpts() {
    }

    @Test
    void findOpt() {
        String[] argv = {"-a","--optarg","66"};
        String opt = CmdArgUtil.findOpt(argv, null, "optarg", true);
        logger.info("{}",opt);
        opt = CmdArgUtil.findOpt(argv, "a", null, false);
        logger.info("{}",opt);
    }
}