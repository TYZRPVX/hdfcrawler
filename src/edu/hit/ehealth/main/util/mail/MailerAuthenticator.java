package edu.hit.ehealth.main.util.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailerAuthenticator extends Authenticator {
    String userName = null;
    String password = null;

    public MailerAuthenticator() {
    }

    public MailerAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}