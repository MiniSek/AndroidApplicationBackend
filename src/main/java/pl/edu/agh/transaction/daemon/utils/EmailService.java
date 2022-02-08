package pl.edu.agh.transaction.daemon.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.exception.MessageSendingException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailService {
    private final Session session;

    private final String FROM_EMAIL;
    private final String FROM_PERSONAL_EMAIL;

    @Autowired
    public EmailService(@Value("${FROM_EMAIL}") String FROM_EMAIL,
                        @Value("${EMAIL_SERVER_PASSWORD}") String EMAIL_SERVER_PASSWORD,
                        @Value("${FROM_PERSONAL_EMAIL}") String FROM_PERSONAL_EMAIL) {
        this.FROM_EMAIL = FROM_EMAIL;
        this.FROM_PERSONAL_EMAIL = FROM_PERSONAL_EMAIL;

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        properties.put("mail.smtp.port", "587"); //TLS Port
        properties.put("mail.smtp.auth", "true"); //enable authentication
        properties.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_SERVER_PASSWORD);
            }
        };

       this.session = Session.getInstance(properties, authenticator);
    }

    public void sendEmail(String recipientEmail, String subject, String body) throws MessageSendingException {
        try {
            MimeMessage message = buildSimpleMessage(recipientEmail, subject, body);

            Transport.send(message);
        } catch (Exception e) {
            throw new MessageSendingException(e);
        }
    }

    public void sendEmailWithAttachment(String recipientEmail, String subject, String body, String pathToFile)
            throws MessageSendingException {

        try {
            MimeMessage message = buildSimpleMessage(recipientEmail, subject, body);

            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(new File(pathToFile));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(attachmentBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (Exception e) {
            throw new MessageSendingException(e);
        }
    }

    private MimeMessage buildSimpleMessage(String recipientEmail, String subject, String body)
            throws MessagingException, UnsupportedEncodingException {

            MimeMessage message = new MimeMessage(session);
            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            message.addHeader("format", "flowed");
            message.addHeader("Content-Transfer-Encoding", "8bit");

            message.setFrom(new InternetAddress(FROM_EMAIL, FROM_PERSONAL_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));
            message.setSentDate(new Date());

            message.setSubject(subject, "UTF-8");
            message.setText(body, "UTF-8");

            return message;
    }
}