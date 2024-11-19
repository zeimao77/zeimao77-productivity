package com.zeimao77;
import com.jcraft.jsch.*;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.LocalDateTimeUtil;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;

public class Main extends BaseMain {

    public static void main(String[] args) throws UnsupportedEncodingException {

        String host = "Transfer.dufry.com";
        int port = 22;
        String user = "ftp_cn_pos";
        String password = "b38*kMn&/Kxt=N.yg;Rd"; // 替换为实际密码\

        AvoltaSftpConfig avoltaSftpConfig = new AvoltaSftpConfig();
        avoltaSftpConfig.setHost(host);
        avoltaSftpConfig.setPort(port);
        avoltaSftpConfig.setUser(user);
        avoltaSftpConfig.setPassword(password);
        // AvoltaSftpClient avoltaSftpClient = new AvoltaSftpClient(avoltaSftpConfig);

        String flagTime = LocalDateTimeUtil.toDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        String dateTime = LocalDateTimeUtil.toDateTime(LocalDateTime.now());
        HashMap<String,String> redis = new HashMap<>();
        String key = "avolta_create_info";
        int res_number = 1;
        redis.put(key,"1");
        redis.put(key+"_lastTime","2024-11-18 00:00:00");
        redis.put(key+"_number","3");

        if(redis.containsKey(key)) {
            String lastTime = redis.get(key+"_lastTime");
            String number_str = redis.get(key+"_number");
            Integer number = Integer.valueOf(number_str);
            if(lastTime.compareTo(flagTime) < 0) {
                redis.put(key+"_lastTime",dateTime);
                redis.put(key+"_number","2");
                res_number = 1;
            } else {
                redis.put(key+"_lastTime",dateTime);
                redis.put(key+"_number",String.valueOf(number+1));
                res_number = number;
            }

        } else {
            redis.put(key,"1");
            redis.put(key+"_lastTime",dateTime);
            redis.put(key+"_number","2");
            res_number = 1;
        }

        logger.info("获取到number:{}",res_number);

    }

    public static class AvoltaSftpConfig {
        private String host;
        private String password;

        private String user;

        private int port;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }


    public static class AvoltaSftpClient implements AutoCloseable{

        private AvoltaSftpConfig config;

        private Session session;

        private ChannelSftp sftpChannel;

        public static final String LOCK_FILE = "/Sales/SHIJI/SZX/.lock";

        public AvoltaSftpClient(AvoltaSftpConfig voaltaSftpConfig) {
            this.config = voaltaSftpConfig;
        }

        public void init() throws JSchException {
            JSch jsch = new JSch();
            session = jsch.getSession(config.user, config.host, config.port);
            session.setPassword(config.password);

            // 设置第一次登录时不询问yes/no
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("server_host_key","ssh-rsa");

            // 连接到SFTP服务器
            session.connect();

            // 创建SFTP通道
            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftpChannel = (ChannelSftp) channel;
        }

        @Override
        public void close() throws Exception {
            sftpChannel.disconnect();
            session.disconnect();
        }

        public ChannelSftp getSftpChannel() {
            return sftpChannel;
        }
    }


}
