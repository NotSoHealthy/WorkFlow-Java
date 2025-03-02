package utils;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class JavaMailSender {
    private final String username;
    private final String password;

    /**
     * Constructs a JavaMailSender with the given SMTP credentials.
     * @param username your email address (e.g., your-email@gmail.com)
     * @param password your app-specific password (do NOT use your real Gmail password)
     */
    public JavaMailSender(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Sends an email with the specified recipient, subject, and body.
     * @param recipientEmail The recipient's email address.
     * @param subject The subject line.
     * @param body The email body text.
     * @throws MessagingException if sending the email fails.
     */
    public void sendEmail(String recipientEmail, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        // Enable SMTP authentication and TLS
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        // Gmail SMTP host and port
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}
