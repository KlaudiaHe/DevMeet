package pl.com.devmeet.devmeetcore.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    //    private DefaultUserLoginTypeEnum login;
//    private String phone;
    private Long id;
    private String email;
    private String password;

    private DateTime creationTime;
    private DateTime modificationTime;

//    private boolean loggedIn;
//    private DateTime loginTime;

    private boolean isActive;
    private UUID activationKey;
    private LocalDateTime lastLoggedIn;

}
