package pl.com.devmeet.devmeetcore.member_associated.member.domain;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.devmeet.devmeetcore.domain_utils.exceptions.CrudException;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupCrudRepository;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.status_and_exceptions.GroupNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.*;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.domain.MessengerRepository;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerAlreadyExistsException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerArgumentNotSpecified;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerNotFoundException;
import pl.com.devmeet.devmeetcore.user.domain.*;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserAlreadyActiveException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserAlreadyExistsException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class MemberCrudServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessengerRepository messengerRepository;
    @Autowired
    private GroupCrudRepository groupCrudRepository;

    private UserDto testUserDto;
    private MemberDto testMemberDto;

    private MemberCrudService memberCrudService;

    private UserCrudService userCrudService;

    @Before
    public void setUp() {
        testUserDto = UserDto.builder()
                .email("test@test.pl")
                .password("testPass")
                .isActive(true)
                .build();

        testMemberDto = MemberDto.builder()
                .user(testUserDto)
                .nick("Wasacz")
                .creationTime(null)
                .modificationTime(null)
                .isActive(false)
                .build();

        memberCrudService = initMemberCrudFacade();
    }

    private UserCrudService initUserCrudFacade() {
        return new UserCrudService(userRepository);
    }

    private MemberDto createMember() throws UserNotFoundException, MemberAlreadyExistsException, GroupNotFoundException, MemberNotFoundException, MessengerAlreadyExistsException, MessengerArgumentNotSpecified, MemberUserNotActiveException {
        return initMemberCrudFacade().add(testMemberDto);
    }

    private MemberCrudService initMemberCrudFacade() {
        return new MemberCrudService(memberRepository, userRepository, messengerRepository, groupCrudRepository);
    }

    private UserEntity initTestDatabaseByAddingUser() {
        UserCrudService userCrudService = initUserCrudFacade();

        try {
            return userCrudService
                    .findEntity(
                            userCrudService.activation(
                                    userCrudService.add(testUserDto)
                            )
                    );
        } catch (UserNotFoundException | UserAlreadyExistsException | UserAlreadyActiveException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void INIT_TEST_DB() {
        UserEntity createdUser = initTestDatabaseByAddingUser();
        assertThat(createdUser).isNotNull();
    }

    @Test
    public void WHEN_try_to_create_member_for_not_activated_user_THEN_return_MemberUserNotActiveException() throws UserAlreadyExistsException, UserNotFoundException {
        UserCrudService userCrudService = initUserCrudFacade();
        userCrudService.add(testUserDto);
        try {
            initMemberCrudFacade().add(testMemberDto);
            Assert.fail();
        } catch (MemberAlreadyExistsException | MemberNotFoundException | GroupNotFoundException | MessengerAlreadyExistsException | MessengerArgumentNotSpecified e) {
            Assert.fail();
        } catch (CrudException e) {
            assertThat(e)
                    .isInstanceOf(MemberUserNotActiveException.class)
                    .hasMessage(MemberCrudStatusEnum.MEMBER_USER_NOT_ACTIVE.toString());
        }
    }

    @Test
    public void WHEN_creating_non_existing_member_for_activated_user_THEN_create_new_member() throws UserNotFoundException, MemberAlreadyExistsException, MemberNotFoundException, GroupNotFoundException, MessengerArgumentNotSpecified, MessengerAlreadyExistsException, MemberUserNotActiveException {
        UserEntity createdUser = initTestDatabaseByAddingUser();
        MemberDto member = createMember();
        MemberEntity memberEntity = initMemberCrudFacade().findEntity(testMemberDto);

        assertThat(member).isNotNull();
        assertThat(member.getNick()).isEqualTo(testMemberDto.getNick());
        assertThat(member.getCreationTime()).isNotNull();
        assertThat(member.getModificationTime()).isNull();
        assertThat(member.isActive()).isTrue();

        assertThat(memberEntity.getUser()).isEqualToComparingFieldByFieldRecursively(createdUser);
    }

    @Test
    public void WHEN_try_to_create_existing_member_for_activated_user_THEN_throw_exception() {
        initTestDatabaseByAddingUser();
        MemberDto createdMember = null;

        try {
            createdMember = createMember();
        } catch (UserNotFoundException | MemberAlreadyExistsException | GroupNotFoundException | MemberNotFoundException | MessengerAlreadyExistsException | MessengerArgumentNotSpecified | MemberUserNotActiveException e) {
            Assert.fail();
        }

        try {
            memberCrudService.add(createdMember);
            Assert.fail();
        } catch (MemberAlreadyExistsException e) {
            assertThat(e)
                    .isInstanceOf(MemberAlreadyExistsException.class)
                    .hasMessage(MemberCrudStatusEnum.MEMBER_ALREADY_EXIST.toString());
        } catch (MessengerAlreadyExistsException | MemberNotFoundException | GroupNotFoundException | MessengerArgumentNotSpecified | MemberUserNotActiveException | UserNotFoundException e) {
            Assert.fail();
        }
    }

    @Test
    public void WHEN_find_existing_member_THEN_return_memberDto() throws UserNotFoundException, MemberAlreadyExistsException, MemberNotFoundException, GroupNotFoundException, MessengerArgumentNotSpecified, MessengerAlreadyExistsException, MemberUserNotActiveException {
        initTestDatabaseByAddingUser();
        UserDto foundUser = initUserCrudFacade().find(testUserDto);
        createMember();

        MemberDto foundMemberDto = memberCrudService.find(testMemberDto);

        assertThat(foundMemberDto).isNotNull();
        assertThat(foundMemberDto.getNick()).isEqualTo(testMemberDto.getNick());

        assertThat(foundMemberDto.getUser()).isEqualToComparingFieldByFieldRecursively(foundUser);
    }

    @Test
    public void WHEN_try_to_find_member_who_does_not_exist_THEN_return_MemberNotFoundException() throws UserNotFoundException {
        initTestDatabaseByAddingUser();

        try {
            memberCrudService.find(testMemberDto);
            Assert.fail();
        } catch (MemberNotFoundException e) {
            assertThat(e)
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage(MemberCrudStatusEnum.MEMBER_NOT_FOUND.toString());
        }
    }

    @Test
    public void WHEN_member_exists_THEN_return_true() throws UserNotFoundException, MemberAlreadyExistsException, MemberNotFoundException, GroupNotFoundException, MessengerAlreadyExistsException, MessengerArgumentNotSpecified, MemberUserNotActiveException {
        initTestDatabaseByAddingUser();
        createMember();

        boolean memberExists = memberCrudService.isExist(testMemberDto);
        assertThat(memberExists).isTrue();
    }

    @Test
    public void WHEN_member_does_not_exist_THEN_return_false() {
        boolean memberDoesNotExist = memberCrudService.isExist(testMemberDto);
        assertThat(memberDoesNotExist).isFalse();
    }

    @Test
    public void WHEN_try_to_update_existing_member_THEN_updated_member() throws UserNotFoundException, MemberAlreadyExistsException, MemberNotFoundException, MemberFoundButNotActiveException, GroupNotFoundException, MessengerArgumentNotSpecified, MessengerAlreadyExistsException, MemberUserNotActiveException {
        initTestDatabaseByAddingUser();
        UserDto foundUser = initUserCrudFacade().find(testUserDto);
        MemberDto createdMember = createMember();

        MemberDto updatedMember = memberCrudService.update(testMemberDto, updateTestMemberDto(testMemberDto));

        assertThat(updatedMember.getUser()).isEqualToComparingFieldByFieldRecursively(foundUser);

        assertThat(updatedMember.isActive()).isEqualTo(createdMember.isActive());
        assertThat(updatedMember.getCreationTime()).isNotNull();
        assertThat(updatedMember.getModificationTime()).isNotNull();
    }

    private MemberDto updateTestMemberDto(MemberDto testMemberDto) {
        testMemberDto.setNick("changedNick");
        return illegalOperations(testMemberDto);
    }

    private MemberDto illegalOperations(MemberDto testMemberDto) {
        testMemberDto.setActive(false);
        testMemberDto.setCreationTime(DateTime.now());
        testMemberDto.setModificationTime(null);
        return testMemberDto;
    }

    @Test
    public void WHEN_try_to_update_existing_but_not_active_member_THEN_return_exception() throws MemberNotFoundException, UserNotFoundException, MemberAlreadyExistsException, MemberFoundButNotActiveException, GroupNotFoundException, MessengerArgumentNotSpecified, MessengerAlreadyExistsException, MessengerNotFoundException, MemberUserNotActiveException {
        initTestDatabaseByAddingUser();
        memberCrudService.add(testMemberDto);
        memberCrudService.delete(testMemberDto);

        try {
            memberCrudService.update(testMemberDto, updateTestMemberDto(testMemberDto));
            Assert.fail();
        } catch (MemberFoundButNotActiveException e) {
            assertThat(e)
                    .isInstanceOf(MemberFoundButNotActiveException.class)
                    .hasMessage(MemberCrudStatusEnum.MEMBER_FOUND_BUT_NOT_ACTIVE.toString());
        }
    }

    @Test
    public void WHEN_try_to_update_not_existing_member_THEN_return_MemberNotFoundException() throws UserNotFoundException, MemberFoundButNotActiveException {
        initTestDatabaseByAddingUser();

        try {
            memberCrudService.update(testMemberDto, updateTestMemberDto(testMemberDto));
            Assert.fail();
        } catch (MemberNotFoundException e) {
            assertThat(e)
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage(MemberCrudStatusEnum.MEMBER_NOT_FOUND.toString());
        }
    }

    @Test
    public void WHEN_try_to_delete_existing_member_THEN_delete_member() throws MemberFoundButNotActiveException, MemberNotFoundException, UserNotFoundException, MemberAlreadyExistsException, GroupNotFoundException, MessengerArgumentNotSpecified, MessengerAlreadyExistsException, MessengerNotFoundException, MemberUserNotActiveException {
        initTestDatabaseByAddingUser();
        createMember();

        MemberDto isMemberDeleted = memberCrudService.delete(testMemberDto);
        assertThat(isMemberDeleted.isActive()).isFalse();
    }

    @Test
    public void WHEN_try_to_delete_non_existing_member_THEN_throw_EntityNotFoundException() throws UserNotFoundException, MemberFoundButNotActiveException, MessengerAlreadyExistsException, MessengerNotFoundException, GroupNotFoundException {
        initTestDatabaseByAddingUser();

        try {
            memberCrudService.delete(testMemberDto);
            Assert.fail();
        } catch (MemberNotFoundException e) {
            assertThat(e)
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage(MemberCrudStatusEnum.MEMBER_NOT_FOUND.toString());
        }
    }
}