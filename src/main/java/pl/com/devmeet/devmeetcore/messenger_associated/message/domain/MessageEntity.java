package pl.com.devmeet.devmeetcore.messenger_associated.message.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.domain.MessengerEntity;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "messages")
@Entity
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private MessengerEntity sender;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private MessengerEntity receiver;

    private String message;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private DateTime modificationTime;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private DateTime creationTime;

    private boolean isActive;
}
