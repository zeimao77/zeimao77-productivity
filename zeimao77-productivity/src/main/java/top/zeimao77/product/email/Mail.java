package top.zeimao77.product.email;

import jakarta.mail.internet.MimeMultipart;
import top.zeimao77.product.model.ImmutablePair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;


/**
 * @author zeimao77
 * 邮件实体对象
 */
public class Mail {

    /**
     * @param subject 邮件主题
     */
    public Mail(String subject) {
        this.subject = subject;
    }

    /**
     * @param subject 邮件主题
     * @param content 邮件正文
     */
    public Mail(String subject,String content) {
        this.subject = subject;
        this.content = content;
    }

    /**
     * @param subject 邮件主题
     * @param mimeMultipartConsumer 邮件正文实现
     */
    public Mail(String subject, Consumer<MimeMultipart> mimeMultipartConsumer) {
        this.subject = subject;
        this.con = mimeMultipartConsumer;
    }

    /**
     * 收信人，这是一个目标邮箱地址
     */
    private HashSet<String> toRecipients = new HashSet<>();

    /**
     * 抄送给
     */
    private HashSet<String> ccRecipients = new HashSet<>();

    /**
     * 附件列表
     */
    private List<ImmutablePair<String,byte[]>> attachments;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件正文
     */
    private String content;

    // 设置邮件正文
    // 如果它不为空将忽略contet设置，这里可以添加邮件附件、正文等
    private Consumer<MimeMultipart> con;

    public HashSet<String> getToRecipients() {
        return toRecipients;
    }

    public HashSet<String> getCcRecipients() {
        return ccRecipients;
    }

    /**
     * @param toRecipient 收件人
     * @return 是否添加成功
     */
    public boolean addToRecipients(String toRecipient) {
        return this.toRecipients.add(toRecipient);
    }

    /**
     * @param ccRecipient 抄送人
     * @return 是否添加成功
     */
    public boolean addCcRecipients(String ccRecipient) {
        return this.ccRecipients.add(ccRecipient);
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    /**
     * @param content 正文
     */
    public void setContent(String content) {
        this.content = content;
    }

    public Consumer<MimeMultipart> getCon() {
        return con;
    }

    public void setCon(Consumer<MimeMultipart> con) {
        this.con = con;
    }

    /**
     * 添加附件
     * @param fileName 文件名 后缀名会用来判断文件类型，不可随意更改
     * @param bs 文件字节数组
     * @return 是否添加成功
     */
    public boolean addAttachment(String fileName,byte[] bs) {
        if(attachments == null) {
            synchronized (Mail.class) {
                if(attachments == null) {
                    attachments = new ArrayList<>();
                }
            }
        }
        return attachments.add(new ImmutablePair<>(fileName,bs));
    }

    public List<ImmutablePair<String, byte[]>> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ImmutablePair<String, byte[]>> attachments) {
        this.attachments = attachments;
    }
}
