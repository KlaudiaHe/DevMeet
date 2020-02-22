package pl.com.devmeet.devmeetcore.member_associated.availability.domain;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupCrudRepository;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.status_and_exceptions.GroupNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.status_and_exceptions.AvailabilityAlreadyExistsException;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.status_and_exceptions.AvailabilityCrudInfoStatusEnum;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.status_and_exceptions.AvailabilityException;
import pl.com.devmeet.devmeetcore.member_associated.availability.domain.status_and_exceptions.AvailabilityNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberCrudService;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberDto;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberEntity;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberRepository;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberAlreadyExistsException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberUserNotActiveException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.domain.MessengerRepository;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerAlreadyExistsException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerArgumentNotSpecified;
import pl.com.devmeet.devmeetcore.user.domain.*;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserAlreadyActiveException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserAlreadyExistsException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)

public class AvailabilityCrudServiceTest {

    @Autowired
    private AvailabilityCrudRepository repository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessengerRepository messengerRepository;
    @Autowired
    private GroupCrudRepository groupCrudRepository;

    private AvailabilityCrudService availabilityCrudService;
    private MemberCrudService memberCrudService;
    private UserCrudService userCrudService;

    private AvailabilityDto testAvailabilityDto;
    private MemberDto testMemberDto;
    private UserDto testUserDto;
    //   private PlaceDto testPlaceDto;

    @Before
    public void setUp() {

        testUserDto = UserDto.builder()
                .email("testavailabilityuser@gmail.com")
                .password("multiPass")
                .isActive(true)
                .build();

        testMemberDto = MemberDto.builder()
                .user(testUserDto)
                .nick("WEK666")
                .build();

        testAvailabilityDto = AvailabilityDto.builder()
                .member(testMemberDto)
                .beginDateTime(new DateTime(2020, 3, 3, 15, 0, 0))
                .endDateTime(new DateTime(2020, 3, 3, 16, 0, 0))
//                .availabilityVote(null)
                .remoteWork(true)
                .creationTime(null)
                .modificationTime(null)
                .isActive(true)
                .build();
    }

    private UserCrudService initUserCrudFacade() {
        return new UserCrudService(userRepository);
    }

    private MemberCrudService initMemberCrudFacade() {
        return new MemberCrudService(memberRepository, userRepository, messengerRepository, groupCrudRepository); // tworzy obiekt fasady
    }

    private AvailabilityCrudService initAvailabilityCrudFacade() {
        return new AvailabilityCrudService(repository, memberRepository, userRepository, messengerRepository, groupCrudRepository);
    }


    private boolean initTestDB() {
        userCrudService = initUserCrudFacade();
        memberCrudService = initMemberCrudFacade();

        UserEntity testUser = null;
        try {
            testUser = userCrudService
                    .findEntity(
                            userCrudService.activation(
                                    userCrudService.add(testUserDto)
                            )
                    );
        } catch (UserNotFoundException | UserAlreadyExistsException | UserAlreadyActiveException e) {
            e.printStackTrace();
        }

        MemberEntity memberEntity = null;
        try {
            memberEntity = memberCrudService
                    .findEntity(memberCrudService.add(testMemberDto));
        } catch (MemberNotFoundException | MemberAlreadyExistsException | UserNotFoundException | GroupNotFoundException | MessengerAlreadyExistsException | MessengerArgumentNotSpecified | MemberUserNotActiveException e) {
            e.printStackTrace();
        }

        return testUser != null
                && memberEntity != null;
    }

    @Test
    public void USER_CRUD_FACADE_WR() throws UserAlreadyExistsException, UserNotFoundException {
        UserCrudService userCrudService = initUserCrudFacade();
        UserDto testUser = userCrudService.add(testUserDto);
        UserEntity userEntity = userCrudService.findEntity(testUser);
        assertThat(userEntity).isNotNull();
    }

    @Test
    public void MEMBER_CRUD_FACADE_WR() throws UserNotFoundException, MemberAlreadyExistsException, MemberNotFoundException, GroupNotFoundException, MessengerArgumentNotSpecified, MessengerAlreadyExistsException, UserAlreadyExistsException, MemberUserNotActiveException, UserAlreadyActiveException {
        MemberCrudService memberCrudService = initMemberCrudFacade();
        UserCrudService userCrudService = initUserCrudFacade();
        userCrudService.add(testUserDto);
        userCrudService.activation(testUserDto);
        MemberEntity memberEntity = memberCrudService.findEntity(memberCrudService.add(testMemberDto));
        assertThat(memberEntity).isNotNull();
    }

    @Test
    public void INIT_TEST_DB() {
        boolean initDB = initTestDB();
        assertThat(initDB).isTrue();
    }

    @Test
    public void WHEN_try_to_create_non_existing_availability_THEN_return_availability() throws MemberNotFoundException, AvailabilityAlreadyExistsException, UserNotFoundException {
        initTestDB();
        availabilityCrudService = initAvailabilityCrudFacade();
        AvailabilityDto created = availabilityCrudService.add(testAvailabilityDto);

        assertThat(created.getMember()).isNotNull();
        assertThat(created).isNotNull();
        assertThat(created.getCreationTime()).isNotNull();
        assertThat(created.getModificationTime()).isNull();
        assertThat(created.isActive()).isTrue();
    }

    @Test
    public void WHEN_try_to_create_existing_availability_THEN_EntityAlreadyExistsException() throws MemberNotFoundException, UserNotFoundException {
        initTestDB();
        availabilityCrudService = initAvailabilityCrudFacade();
        try {
            availabilityCrudService.add(testAvailabilityDto);
        } catch (AvailabilityAlreadyExistsException e) {
            Assert.fail();
        }
        try {
            availabilityCrudService.add(testAvailabilityDto);
            Assert.fail();
        } catch (AvailabilityAlreadyExistsException e) {
            assertThat(e)
                    .isInstanceOf(AvailabilityAlreadyExistsException.class)
                    .hasMessage(AvailabilityCrudInfoStatusEnum.AVAILABILITY_ALREADY_EXISTS.toString());
        }
    }

    @Test
    public void WHEN_found_availability_THEN_return_availability() throws MemberNotFoundException, AvailabilityAlreadyExistsException, UserNotFoundException, AvailabilityNotFoundException {
        initTestDB();
        AvailabilityDto created;
        AvailabilityDto found = null;
        availabilityCrudService = initAvailabilityCrudFacade();

        created = availabilityCrudService.add(testAvailabilityDto);
        found = availabilityCrudService.find(testAvailabilityDto);
        assertThat(found).isNotNull();
    }

    @Test
    public void WHEN_try_to_find_non_existing_availability_THEN_return_EntityNotFoundException() throws MemberNotFoundException, UserNotFoundException {
        initTestDB();
        availabilityCrudService = initAvailabilityCrudFacade();
        try {
            availabilityCrudService.find(testAvailabilityDto);
            Assert.fail();
        } catch (AvailabilityNotFoundException e) {
            assertThat(e)
                    .isInstanceOf(AvailabilityNotFoundException.class)
                    .hasMessage(AvailabilityCrudInfoStatusEnum.AVAILABILITY_NOT_FOUND.toString());
        }
    }

    @Test
    public void WHEN_try_to_find_all_availabilities_THEN_return_availabilities() throws MemberNotFoundException, AvailabilityAlreadyExistsException, UserNotFoundException, AvailabilityNotFoundException {
        initTestDB();
        List<AvailabilityDto> found = null;
        AvailabilityCrudService availabilityCrudService = initAvailabilityCrudFacade();
        availabilityCrudService.add(testAvailabilityDto);
        found = availabilityCrudService.findAll(testAvailabilityDto);
        assertThat(found).isNotNull();
    }

    @Test
    public void WHEN_try_to_update_existing_availability_THEN_return_availability() throws MemberNotFoundException, AvailabilityAlreadyExistsException, UserNotFoundException, AvailabilityNotFoundException, AvailabilityException {
        initTestDB();
        AvailabilityCrudService availabilityCrudService = initAvailabilityCrudFacade();
        AvailabilityDto created = availabilityCrudService.add(testAvailabilityDto);
        AvailabilityDto updated = availabilityCrudService.update(availabilityUpdatedValues(testAvailabilityDto));


        assertThat(updated.isRemoteWork()).isFalse();

        assertThat(updated.getMember()).isEqualToComparingFieldByField(created.getMember());
        assertThat(updated.getBeginDateTime()).isEqualTo(created.getBeginDateTime());
        assertThat(updated.getEndDateTime()).isEqualTo(created.getEndDateTime());
//        assertThat(updated.getAvailabilityVote()).isEqualTo(created.getAvailabilityVote());
        assertThat(updated.getCreationTime()).isEqualTo(created.getCreationTime());
        assertThat(updated.isActive()).isEqualTo(created.isActive());
        //    assertThat(updated.getModificationTime()).isNotEqualTo(created.getModificationTime());
    }

    private AvailabilityDto availabilityUpdatedValues(AvailabilityDto testAvailabilityDto) {
        testAvailabilityDto.setRemoteWork(false);
        return testAvailabilityDto;
    }

    @Test
    public void WHEN_try_to_update_non_existing_availability_THEN_return_EntityNotFoundException() throws UserNotFoundException, MemberNotFoundException, AvailabilityException {
        initTestDB();
        AvailabilityCrudService availabilityCrudService = initAvailabilityCrudFacade();
        try {
            availabilityCrudService.update(availabilityUpdatedValues(testAvailabilityDto));
        } catch (AvailabilityNotFoundException e) {
            assertThat(e)
                    .isInstanceOf(AvailabilityNotFoundException.class)
                    .hasMessage(AvailabilityCrudInfoStatusEnum.AVAILABILITY_NOT_FOUND.toString());
        }
    }

    @Test
    public void WHEN_delete_existing_availability_THEN_return_availability() throws MemberNotFoundException, AvailabilityAlreadyExistsException, UserNotFoundException, AvailabilityNotFoundException {
        initTestDB();
        AvailabilityCrudService availabilityCrudService = initAvailabilityCrudFacade();
        AvailabilityDto created = availabilityCrudService.add(testAvailabilityDto);
        AvailabilityDto deleted = availabilityCrudService.delete(testAvailabilityDto);

        assertThat(deleted).isNotNull();
        assertThat(deleted.isActive()).isNotEqualTo(created.isActive());
        assertThat(deleted.getCreationTime()).isEqualTo(created.getCreationTime());
        assertThat(deleted.getModificationTime()).isNotNull();
    }

    @Test
    public void WHEN_try_to_delete_non_existing_availability_THEN_return_EntityNotFoundException() throws UserNotFoundException, AvailabilityAlreadyExistsException, MemberNotFoundException {
        initTestDB();
        AvailabilityCrudService availabilityCrudService = initAvailabilityCrudFacade();

        try {
            availabilityCrudService.delete(testAvailabilityDto);
        } catch (AvailabilityNotFoundException e) {
            assertThat(e)
                    .isInstanceOf(AvailabilityNotFoundException.class)
                    .hasMessage(AvailabilityCrudInfoStatusEnum.AVAILABILITY_NOT_FOUND.toString());
        }
    }
}