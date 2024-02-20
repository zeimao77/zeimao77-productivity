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
        String[] argv = {"-a","666","--optarg","66"};
        String opt = CmdArgUtil.findOpt(argv, null, "optarg", true);
        logger.info("{}",opt);
    }
}