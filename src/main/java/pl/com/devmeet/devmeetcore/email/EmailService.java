package pl.com.devmeet.devmeetcore.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender emailSender;

    @Autowired
    EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(final Mail mail) {
        Thread thread = new Thread(() -> {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(mail.getTo());
            msg.setSubject(mail.getSubject());
            msg.setFrom(mail.getFrom());
            msg.setText(mail.getContent());
            try {
                emailSender.send(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
