package edu.hit.ehealth.main.util.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;


public class MailSender {
    /**
     * 以HTML格式发送邮件
     *
     * @param mailInfo 待发送的邮件信息
     * @return 是否正常发送
     */
    public static boolean sendHtmlMail(MailSenderInfo mailInfo) {
        // 判断是否需要身份认证
        MailerAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();

        if (mailInfo.isValidate()) {
            authenticator = new MailerAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }

        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
        try {

            Message mailMessage = new MimeMessage(sendMailSession);

            Address from = new InternetAddress(mailInfo.getFromAddress());

            mailMessage.setFrom(from);

            Address to = new InternetAddress(mailInfo.getToAddress());

            mailMessage.setRecipient(Message.RecipientType.TO, to);

            mailMessage.setSubject(mailInfo.getSubject());

            mailMessage.setSentDate(new Date());

            Multipart mainPart = new MimeMultipart();

            BodyPart html = new MimeBodyPart();

            html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);

            mailMessage.setContent(mainPart);

            Transport.send(mailMessage);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 以文本格式发送邮件
     *
     * @param mailInfo 待发送的邮件的信息
     * @return 是否正常发送
     */
    public boolean sendTextMail(MailSenderInfo mailInfo) {
        // 判断是否需要身份认证
        MailerAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        if (mailInfo.isValidate()) {
            authenticator = new MailerAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        Session session = Session.getDefaultInstance(pro, authenticator);
        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(mailInfo.getFromAddress()));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mailInfo.getToAddress()));
            message.setSubject(mailInfo.getSubject());
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(mailInfo.getContent());
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            // Part two is attachment
            //XXX: uncomment to send log file
//            messageBodyPart = new MimeBodyPart();
//            String filename = TextValue.Path.errorlog;
//            FileDataSource source = new FileDataSource(filename);
//            messageBodyPart.setDataHandler(new DataHandler(source));
//            messageBodyPart.setFileName(filename);
//            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Sent message successfully....");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
