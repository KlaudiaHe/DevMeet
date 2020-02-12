package pl.com.devmeet.devmeetcore.user.domain;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.com.devmeet.devmeetcore.email.EmailService;
import pl.com.devmeet.devmeetcore.email.Mail;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.InvalidUUIDStringException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserAlreadyActiveException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserCrudStatusEnum;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository repository;
    private EmailService emailService;

    @Autowired
    public UserService(UserRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    // find

    public Optional<UserDto> findById(Long id) {
        return repository.findById(id)
                .map(UserCrudService::map);
    }

    public List<UserDto> findAll() {
        return repository.findAll()
                .stream()
                .map(UserCrudService::map)
                .collect(Collectors.toList());
    }

    public Optional<UserDto> findByEmail(String email) {
        return repository.findByEmailIgnoreCase(email)
                .map(UserCrudService::map);
    }

    public List<UserDto> findEmailLike(String text) {
        return repository.findEmailLike(text)
                .stream()
                .map(UserCrudService::map)
                .collect(Collectors.toList());
    }

    public List<UserDto> findAllByIsActive(Boolean isActive) {
        return repository.findAllByIsActive(isActive)
                .stream()
                .map(UserCrudService::map)
                .collect(Collectors.toList());
    }

    // add

    public UserDto add(UserDto user) {
        if (user.getEmail() != null) { // add more ifs statements if required
            //todo validate email address
            checkEmailDuplication(user);
            user.setActivationKey(UUID.randomUUID());
            user.setEmail(user.getEmail().toLowerCase());
            user.setCreationTime(DateTime.now());
            user.setPassword(String.valueOf(generateRandomPassword()));
            if (!user.isActive()) {
                sendMessageToActivateUser(
                        user.getEmail(),
                        "Devmeet user registration",
                        user.getActivationKey().toString(),
                        user.getPassword());
            }
            return mapAndSave(user);
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "Adding user error!. Set all required fields without id or use update.");
    }

    // update

    public UserDto update(UserDto user) {
        Optional<UserEntity> first = repository.findById(user.getId());
        if (first.isPresent()) {
            if (user.getEmail() == null) {
                user.setEmail(first.get().getEmail());
            } else checkEmailDuplication(user);
            if (user.getPassword() == null) user.setPassword(first.get().getPassword());
            user.setCreationTime(first.get().getCreationTime());
            user.setModificationTime(DateTime.now());
            return mapAndSave(user);
        } else
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Resource with id (" + user.getId() + ") Not found");
    }

    // delete

    public boolean delete(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    // custom methods

    private UserDto mapAndSave(UserDto user) {
        UserEntity userEntity = UserCrudService.map(user);
        UserEntity savedUser = repository.save(userEntity);
        return UserCrudService.map(savedUser);
    }

    private void checkEmailDuplication(UserDto user) {
        Optional<UserEntity> userByEmail = repository.findByEmailIgnoreCase(user.getEmail());
        if (userByEmail.isPresent()) {
            if (!userByEmail.get().getId().equals(user.getId()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Email: " + user.getEmail() + " already assigned for user id = " + userByEmail.get().getId());
        }
    }

    private int generateRandomPassword() {
        int min = 1000;
        int max = 9999;
        Random r = new Random();
        return r.ints(min, (max + 1)).limit(1).findFirst().getAsInt();

    }


    public String activateUser(String email, String userKey) throws UserNotFoundException, UserAlreadyActiveException {
        UserDto user = findByEmail(email).orElseThrow(() -> new UserNotFoundException(UserCrudStatusEnum.USER_NOT_FOUND.toString()));
        if (!user.isActive()) {
            try {
                UUID uuid = UUID.fromString(userKey);
                if (user.getActivationKey().equals(uuid)) {
                    user.setActive(true);
                    update(user);
                    //todo create member
                    return "User is active and TODO member created";
                } else throw new InvalidUUIDStringException("Wrong key!");
            } catch (IllegalArgumentException ex) {
                throw new InvalidUUIDStringException(ex.getMessage());
            }
        } else throw new UserAlreadyActiveException(UserCrudStatusEnum.USER_ALREADY_ACTIVE.toString());
    }

    public void sendMessageToActivateUser(String to, String subject, String activationKey, String initialPassword) {
        Mail mail = new Mail();
        mail.setFrom("no-reply@devmeet.com");
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setContent(to, activationKey, initialPassword);
        emailService.sendSimpleMessage(mail);
    }
}
