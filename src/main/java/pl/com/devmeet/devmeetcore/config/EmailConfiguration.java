package pl.com.devmeet.devmeetcore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfiguration {

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setDefaultEncoding("UTF-8");
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setUsername("emailt407@gmail.com");
        javaMailSender.setPassword("POlk@407");
        javaMailSender.setPort(587);
        javaMailSender.setProtocol("smtp");

        Properties mailProperties = new Properties();
        mailProperties.setProperty("mail.smtp.auth", "true");
        mailProperties.setProperty("mail.smtp.starttls.enable", "true");
        mailProperties.setProperty("mail.smtp.connectiontimeout", "6000");
        mailProperties.setProperty("mail.smtp.timeout", "6000");
        mailProperties.setProperty("mail.smtp.writetimeout", "6000");

        javaMailSender.setJavaMailProperties(mailProperties);

        return javaMailSender;
    }

}
