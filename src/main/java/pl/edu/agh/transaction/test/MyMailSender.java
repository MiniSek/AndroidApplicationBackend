package pl.edu.agh.transaction.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.transaction.daemon.EmailService;


@RestController
@RequestMapping(path="api/v1/mail")
public class MyMailSender {
    private final EmailService emailService;

    @Autowired
    public MyMailSender(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    public void sendMail() {
        String toEmail = "dominiksulik20@gmail.com";
        emailService.sendMessage(toEmail,"Ala ma kota a kot ma ale", "Ha ha ha ha tak");
        emailService.sendMessageWithAttachment(toEmail, "zalacznik", "tresc", "C:\\Users\\Dominik\\Desktop\\xd.jpg");
    }
}
