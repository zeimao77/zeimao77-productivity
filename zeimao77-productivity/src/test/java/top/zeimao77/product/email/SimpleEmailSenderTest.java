package top.zeimao77.product.email;

import org.junit.jupiter.api.Test;
import top.zeimao77.product.config.LocalContext;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.StringOptional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SimpleEmailSenderTest extends BaseMain {

    public static final String MAIL_BODY = """
            鸿章窃以为天下事穷则变，变则通.\\r\\n中国士大夫沉浸于章句小楷之积习，武夫悍卒又多粗蠢而不加细心，以致用非所学，学非所用。
            无事则斥外国之利器为奇技淫巧，以为不必学；有事则惊外国之利器为变怪神奇，以为不能学。不知洋人视火器为身心性命之学者已数百年。
            一旦豁然贯通，参阴阳而配造化，实有指挥如意，从心所欲之快。……前者英、法各国，以日本为外府，肆意诛求。日本君臣发愤为雄
            ，选宗室及大臣子弟之聪秀者往西国制造厂师习各艺，又购制器之器在本国制习。现在已能驾驶轮船，造放炸炮。去年英人虚声恫揭
            ,以兵临之。然英人所恃而为攻战之利者，彼已分擅其长，用是凝然不动，而英人固无如之何也，夫今之日本即明之倭寇也
            ，距西国远而距中国近。我有以自立，则将附丽于我，窥视西人之短长；我无以自强，则并效尤于彼，分西人之利薮。日本以海外区区小国
            ，尚能及时改辙，知所取法。然则我中国深维穷权而通之故，夫亦可以皇然变计矣。\\r\\n杜挚有言曰：利不百，不变法；功不十，不易器
            。苏子瞻曰：言之于无事之时，足以为名，而恒苦于不信；言之于有事之时，足以见信，而已苦于无及。\\r\\n鸿章以为
            ，中国欲自强则莫如学习外国利器。欲学习外国利器则莫如觅制器之器，师其法而不必尽用其人。欲觅制器之器与制器之人，则我专设一科取士
            ，士终身悬以为富贵功名之鹄，则业可成，业可精，而才亦可集。
            """;

    @Test
    public void sendEmail() throws IOException {
<<<<<<< HEAD
        String smtpHost = LocalContext.getString("top.zeimao77.product.email.SimpleEmailSenderTest.smtpHost").get();
        String from = LocalContext.getString("top.zeimao77.product.email.SimpleEmailSenderTest.from").get();
        String username = LocalContext.getString("top.zeimao77.product.email.SimpleEmailSenderTest.username").get();
        String password = LocalContext.getString("top.zeimao77.product.email.SimpleEmailSenderTest.password").get();
        String toRecipient = LocalContext.getString("top.zeimao77.product.email.SimpleEmailSenderTest.toRecipient").get();
        String ccRecipient = LocalContext.getString("top.zeimao77.product.email.SimpleEmailSenderTest.ccRecipient").get();
        SimpleEmailSender sender = new SimpleEmailSender(smtpHost,from);
=======
        StringOptional smtpHost = LocalContext.getString("top.zeimao77.product.email.SimpleEmailSenderTest.smtpHost");
        StringOptional from = LocalContext.getString("top.zeimao77.product.email.SimpleEmailSenderTest.from");
        StringOptional username = LocalContext.getString("top.zeimao77.product.email.SimpleEmailSenderTest.username");
        StringOptional password = LocalContext.getString("top.zeimao77.product.email.SimpleEmailSenderTest.password");
        StringOptional toRecipient = LocalContext.getString("top.zeimao77.product.email.SimpleEmailSenderTest.toRecipient");
        StringOptional ccRecipient = LocalContext.getString("top.zeimao77.product.email.SimpleEmailSenderTest.ccRecipient");
        SimpleEmailSender sender = new SimpleEmailSender(smtpHost.get(),from.get());
>>>>>>> main
        Mail mail = null;
        sender.authenticator(username.get(),password.get());
        /**
        mail = new Mail("测试邮件",(o) -> {
            try {
                MimeBodyPart htmlBodyPart = new MimeBodyPart();
                htmlBodyPart.setContent("<h3>李鸿章写给恭亲王和文祥的信(同治三年/1864年)</h3>" +
                    "<p>鸿章窃以为天下事穷则变，变则通。</p>" +
                    "<p>中国士大夫沉浸于章句小楷之积习，武夫悍卒又多粗蠢而不加细心，以致用非所学，学非所用。无事则斥外国之利器为奇技淫巧" +
                    "，以为不必学；有事则惊外国之利器为变怪神奇，以为不能学。不知洋人视火器为身心性命之学者已数百年。一旦豁然贯通" +
                    "，参阴阳而配造化，实有指挥如意，从心所欲之快。……前者英、法各国，以日本为外府，肆意诛求。日本君臣发愤为雄" +
                    "，选宗室及大臣子弟之聪秀者往西国制造厂师习各艺，又购制器之器在本国制习。现在已能驾驶轮船，造放炸炮。去年英人虚声恫揭" +
                    "，以兵临之。然英人所恃而为攻战之利者，彼已分擅其长，用是凝然不动，而英人固无如之何也，夫今之日本即明之倭寇也" +
                    "，距西国远而距中国近。我有以自立，则将附丽于我，窥视西人之短长；我无以自强，则并效尤于彼，分西人之利薮。" +
                    "日本以海外区区小国，尚能及时改辙，知所取法。然则我中国深维穷权而通之故，夫亦可以皇然变计矣。</p>" +
                    "<p>杜挚有言曰：利不百，不变法；功不十，不易器。苏子瞻曰：言之于无事之时，足以为名，而恒苦于不信；言之于有事之时" +
                    "，足以见信，而已苦于无及。</p>" +
                    "<p>鸿章以为，中国欲自强则莫如学习外国利器。欲学习外国利器则莫如觅制器之器，师其法而不必尽用其人。" +
                    "欲觅制器之器与制器之人，则我专设一科取士，士终身悬以为富贵功名之鹄，则业可成，业可精，而才亦可集。</p>"
                    ,"text/html;charset=utf-8");
                o.addBodyPart(htmlBodyPart);

                out.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
         **/
        mail = new Mail("测试邮件","李鸿章写给恭亲王和文祥的信(同治三年/1864年)");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write((MAIL_BODY).getBytes(StandardCharsets.UTF_8));
        mail.addAttachment("content.txt",out.toByteArray());
        out.close();
        mail.addToRecipients(toRecipient.get());
        mail.addCcRecipients(ccRecipient.get());
        sender.send(mail);
    }

}