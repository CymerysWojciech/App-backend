package pl.budowniczowie.appbackend.mail.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {
    private final JavaMailSender javaMailSender;
@Autowired
    MailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public void sendEmail(
            String to,
            String subject,
            String text
    )  {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {
            System.out.println("Sending email to: " + to + " with subject: " + subject + " and content: " + text);

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, false);

        }catch (MessagingException e){
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
    }
}
