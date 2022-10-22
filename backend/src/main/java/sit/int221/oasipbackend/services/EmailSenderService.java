package sit.int221.oasipbackend.services;

import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


@Service
public class EmailSenderService {
    private final JavaMailSender mailSender;

    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail (String toMail , String subject , String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("phulakornsr@gmail.com");
        msg.setTo(toMail);
        msg.setText(body);
        msg.setSubject(subject);

        mailSender.send(msg);
        System.out.println("Email Sent successfully");
    }
}
