package pl.com.devmeet.devmeetcore.user.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.joda.deser.DateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private DefaultUserLoginTypeEnum login;

    private String password;

    @Column(unique = true)
    @Email
    @NotNull
    @NotEmpty
    private String email;

//    private String phone;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private MemberEntity member;

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private DateTime creationTime;

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private DateTime modificationTime;

    @Column(columnDefinition = "boolean default false")
    private boolean isActive;
//
//    private boolean loggedIn;
//
//    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
//    @JsonDeserialize(using = DateTimeDeserializer.class)
//    private DateTime loginTime;

    private UUID activationKey;
    private LocalDateTime lastLoggedIn;

}
