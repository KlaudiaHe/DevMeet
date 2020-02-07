package pl.com.devmeet.devmeetcore.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class EmailService {

    private JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendMessageToActivateUser(String to, String subject, String activationKey, String initialPassword) {
        Thread thread = new Thread(() -> {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            UriComponents uri = UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .host("localhost")
                    .port("8080")
                    .path("/user-activate/" + to + "/" + activationKey)
                    .build()
                    .encode();
            String url = uri.toUriString();
            msg.setText(
                    "Hello " + to + " ,\n\r\n\rthank you for registering your account on Devmeet platform. " +
                            "Activate it within 24 h by clicking on below link:\n\r\n\r" +
                            url + "\n\r\n\rYour initial password is: " + initialPassword + "\n\r\n\rRegards\n\rDevmeet Team");
            try {
                this.emailSender.send(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
