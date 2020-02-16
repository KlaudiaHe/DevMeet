package pl.com.devmeet.devmeetcore.email;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class Mail {
    private String from;
    private String to;
    private String subject;
    private String activationKey;
    private String initialPassword;
    private String content;

    public Mail() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getInitialPassword() {
        return initialPassword;
    }

    public void setInitialPassword(String initialPassword) {
        this.initialPassword = initialPassword;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String to, String activationKey, String initialPassword) {
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port("8080")
                .path("/user-activate/" + to + "/" + activationKey)
                .build()
                .encode();
        String url = uri.toUriString();
        this.content = "Hello " + to + " ,\n\n\rYour Devmeet account has been created, please click on the URL below within 24 h to activate it:\n\n\r" +
                url + "\n\n\rYour initial password is: " + initialPassword + "\n\r\n\rRegards,\n\rDevmeet Team.";
    }

    @Override
    public String toString() {
        return "Mail{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", activationKey='" + activationKey + '\'' +
                ", initialPassword='" + initialPassword + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
