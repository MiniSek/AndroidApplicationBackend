package pl.edu.agh.transaction.daemon;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailService {
    private final Properties properties;
    private final Authenticator authenticator;
    private final Session session;
    private final String fromEmail = "transaction.application.ms@gmail.com";
    private final String password = "gxdxrulvkzppwypl";
    private final String personalEmail = "Frog-image-every-day";

    public EmailService() {
        this.properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        properties.put("mail.smtp.port", "587"); //TLS Port
        properties.put("mail.smtp.auth", "true"); //enable authentication
        properties.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        this.authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

       this.session = Session.getInstance(properties, authenticator);
    }

    public void sendMessage(String recipientEmail, String subject, String body) {
        try {
            MimeMessage message = buildMessageWithoutBody(recipientEmail, subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessageWithAttachment(String recipientEmail, String subject, String body, String pathToFile) {
        try {
            MimeMessage message = buildMessageWithoutBody(recipientEmail, subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, "text/html; charset=utf-8");

            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(new File(pathToFile));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            //multipart.addBodyPart(attachmentBodyPart);

            message.setContent(multipart);

            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MimeMessage buildMessageWithoutBody(String recipientEmail, String subject) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            message.addHeader("format", "flowed");
            message.addHeader("Content-Transfer-Encoding", "8bit");

            message.setFrom(new InternetAddress(fromEmail, personalEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));
            message.setSentDate(new Date());

            message.setSubject(subject, "UTF-8");

            return message;
        } catch(Exception e) {
            throw new IllegalArgumentException("");
        }
    }
}