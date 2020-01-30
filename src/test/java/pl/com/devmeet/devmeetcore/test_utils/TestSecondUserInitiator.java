package pl.com.devmeet.devmeetcore.test_utils;

import pl.com.devmeet.devmeetcore.user.domain.UserCrudService;
import pl.com.devmeet.devmeetcore.user.domain.UserDto;
import pl.com.devmeet.devmeetcore.user.domain.UserRepository;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserAlreadyActiveException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserAlreadyExistsException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;


class TestSecondUserInitiator implements TestObjectInitiator<UserRepository, UserCrudService, UserDto> {

    private UserRepository repository;
    private UserDto testUserDto;

    public TestSecondUserInitiator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserCrudService initFacade() {
        return new UserCrudService(repository);
    }

    @Override
    public UserDto initAndSaveTestObject() throws UserAlreadyExistsException, UserNotFoundException, UserAlreadyActiveException {
        UserCrudService userCrudService = initFacade();
        userCrudService.add(testUserDto);
        return userCrudService.activation(testUserDto);
    }
}
