package pl.com.devmeet.devmeetcore.user.domain;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    // find

    public Optional<UserDto> findById(Long id) {
        return repository.findById(id)
                .map(UserCrudFacade::map);
    }

    public List<UserDto> findAll() {
        return repository.findAll()
                .stream()
                .map(UserCrudFacade::map)
                .collect(Collectors.toList());
    }

    public Optional<UserDto> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(UserCrudFacade::map);
    }

    public List<UserDto> findEmailLike(String text) {
        return repository.findEmailLike(text)
                .stream()
                .map(UserCrudFacade::map)
                .collect(Collectors.toList());
    }

    public List<UserDto> findAllByIsActive(Boolean isActive) {
        return repository.findAllByIsActive(isActive)
                .stream()
                .map(UserCrudFacade::map)
                .collect(Collectors.toList());
    }

    // add

    public UserDto add(UserDto user) {
        if (user.getPassword() != null
                && user.getEmail() != null) { // add more ifs statements if required
//            checkEmailDuplication(user);
            user.setActive(false);
            user.setCreationTime(DateTime.now());
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
        UserEntity userEntity = UserCrudFacade.map(user);
        UserEntity savedUser = repository.save(userEntity);
        return UserCrudFacade.map(savedUser);
    }

    private void checkEmailDuplication(UserDto user) {
        Optional<UserEntity> userByEmail = repository.findByEmail(user.getEmail());
        if (userByEmail.isPresent()) {
            if (!userByEmail.get().getId().equals(user.getId()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Email: " + user.getEmail() + " already assigned for user id = " + userByEmail.get().getId());
        }
    }


}