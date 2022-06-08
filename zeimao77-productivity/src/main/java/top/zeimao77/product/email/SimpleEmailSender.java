package top.zeimao77.product.email;

import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.model.ImmutablePair;


import java.util.List;
import java.util.Properties;

/**
 * @author zeimao77
 * Mail发送者
 */
public class SimpleEmailSender {

    private Logger logger = LogManager.getLogger(SimpleEmailSender.class);

    protected String smtpHost;
    protected String from;
    protected Session session;

    /**
     * 构建邮件发送者
     * @param smtpHost smtp主机
     * @param from  发件人邮箱
     */
    public SimpleEmailSender(String smtpHost,String from){
        this.smtpHost = smtpHost;
        this.from = from;
    }

    /**
     * 认证
     * @param username 用户名，通常为发件人
     * @param password 密码
     */
    public void authenticator(String username,String password){
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", smtpHost);
        Authenticator authenticator = new Authenticator () {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
        this.session = Session.getDefaultInstance(props, authenticator);
    }

    /**
     * 发送邮件,如果 mail.getCon() 返回不为空，将调用进行处理
     * 它可以自己创建邮件正文以及添加附件
     * 否则将使用mail.getContent()作为正文 ，它是一个text/html;的字符串
     * @param mail 邮件
     */
    public void send(Mail mail) {
        if(session == null) {
            throw new BaseServiceRunException("需要先调用authenticator(username,password)认证");
        }
        final MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            for (String toRecipient : mail.getToRecipients()) {
                message.setRecipients(Message.RecipientType.TO,toRecipient);
            }
            for (String ccRecipient : mail.getCcRecipients()) {
                message.setRecipients(Message.RecipientType.CC,ccRecipient);
            }
            message.setSubject(mail.getSubject(), "UTF-8");
            MimeMultipart multipart = new MimeMultipart();
            if(mail.getCon() == null) {
                MimeBodyPart htmlBodyPart = new MimeBodyPart();
                htmlBodyPart.setContent(mail.getContent(),"text/html;charset=utf-8");
                multipart.addBodyPart(htmlBodyPart);
                addAttachment(mail,multipart);
            } else {
                mail.getCon().accept(multipart);
            }
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new BaseServiceRunException("发送邮件错误",e);
        }
    }

    /**
     * 将mail中的附件添加进multipart
     * @param mail 附件
     * @param multipart 容器
     */
    private void addAttachment(Mail mail,MimeMultipart multipart) {
        List<ImmutablePair<String, byte[]>> attachments = mail.getAttachments();
        if(attachments != null && !attachments.isEmpty()) {
            for (ImmutablePair<String, byte[]> attachment : attachments) {
                String fileName = attachment.getLeft();
                String mimeType = null;
                if(fileName.endsWith(".txt") || fileName.endsWith(".log")) {
                    mimeType = "text/plain; charset=UTF-8";
                }else if(fileName.endsWith(".json")) {
                    mimeType = "application/json;charset=UTF-8";
                }else if(fileName.endsWith(".pdf")) {
                    mimeType = "application/pdf";
                }else if(fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")) {
                    mimeType = "image/jpeg";
                }else if(fileName.endsWith(".png")) {
                    mimeType = "image/png";
                }else if(fileName.endsWith(".xlsx")) {
                    mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                }else if(fileName.endsWith(".xls")) {
                    mimeType = "application/vnd.ms-excel";
                }else if(fileName.endsWith(".docx")) {
                    mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                }else if(fileName.endsWith(".doc")) {
                    mimeType = "application/msword";
                }else if(fileName.endsWith(".zip")) {
                    mimeType = "application/zip";
                }else if(fileName.endsWith(".rar")) {
                    mimeType = "application/x-rar-compressed";
                }else if(fileName.endsWith(".svg")) {
                    mimeType = "image/svg+xml";
                }
                if(mimeType == null) {
                    mimeType = "application/octet-stream";
                }
                ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(attachment.getRight(),mimeType);
                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                try {
                    mimeBodyPart.setFileName(attachment.getLeft());
                    mimeBodyPart.setDataHandler(new DataHandler(byteArrayDataSource));
                    multipart.addBodyPart(mimeBodyPart);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
