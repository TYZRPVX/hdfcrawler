package edu.hit.ehealth.main.util.mail;


import edu.hit.ehealth.main.util.SiteInspector;

public class Mailer {

    private static final String[] ADDRESS_LIST = {
            "hitehealth_jpc@126.com",
    };

    public static void main(String[] args) {
        sendToAddressList("中文", "中文");
    }

    private static MailSenderInfo getSinaMailInfo() {

        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost("smtp.sina.com");
        mailInfo.setMailServerPort("587");
        mailInfo.setValidate(true);
        mailInfo.setUserName("specialfordev@sina.com");
        mailInfo.setPassword("special4dev");
        mailInfo.setFromAddress("specialfordev@sina.com");
        return mailInfo;
    }

    public static void sendToAddressList(String sub, String content) {
        MailSenderInfo mailInfo = getSinaMailInfo();
        mailInfo.setSubject(sub);
        mailInfo.setContent(content);
        MailSender sms = new MailSender();
        for (String address : ADDRESS_LIST) {
            mailInfo.setToAddress(address);
            sms.sendTextMail(mailInfo);
        }
    }

    public static void reportSiteInspector(String msg) {
        sendToAddressList(msg, SiteInspector.class.getCanonicalName());
    }
}
