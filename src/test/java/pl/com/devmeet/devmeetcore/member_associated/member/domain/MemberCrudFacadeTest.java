package pl.com.devmeet.devmeetcore.member_associated.member.domain;

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
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberAlreadyExistsException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberCrudStatusEnum;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberFoundButNotActiveException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.domain.MessengerRepository;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerAlreadyExistsException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerArgumentNotSpecified;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerNotFoundException;
import pl.com.devmeet.devmeetcore.user.domain.*;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class MemberCrudFacadeTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessengerRepository messengerRepository;
    @Autowired
    private GroupCrudRepository groupCrudRepository;

    UserDto testUserDto;
    MemberDto testMemberDto;

    private MemberCrudFacade memberCrudFacade;

    @Before
    public void setUp() {
        testUserDto = UserDto.builder()
                .email("test@test.pl")
                .phone("221234567")
                .password("testPass")
                .isActive(true)
                .loggedIn(true)
                .build();

        testMemberDto = MemberDto.builder()
                .user(testUserDto)
                .nick("Wasacz")
                .creationTime(null)
                .modificationTime(null)
                .isActive(false)
                .build();

        memberCrudFacade = initMemberCrudFacade();
    }

    private UserCrudFacade initUserCrudFacade() {
        return new UserCrudFacade(userRepository);
    }

    private MemberDto createMember() throws UserNotFoundException, MemberAlreadyExistsException, GroupNotFoundException, MemberNotFoundException, MessengerAlreadyExistsException, MessengerArgumentNotSpecified {
        return initMemberCrudFacade().add(testMemberDto);
    }

    private MemberCrudFacade initMemberCrudFacade() {
        return new MemberCrudFacade(memberRepository, userRepository, messengerRepository, groupCrudRepository);
    }

    private UserEntity initTestDatabaseByAddingUser() {
        UserCrudFacade userCrudFacade = initUserCrudFacade();

        return userCrudFacade
                .findEntity(userCrudFacade
                        .create(testUserDto, DefaultUserLoginTypeEnum.EMAIL));
    }

    @Test
    public void INIT_TEST_DB() {
        UserEntity createdUser = initTestDatabaseByAddingUser();
        assertThat(createdUser).isNotNull();
    }

    @Test
    public void WHEN_creating_non_existing_member_THEN_create_new_member() throws UserNotFoundException, MemberAlreadyExistsException, MemberNotFoundException, GroupNotFoundException, MessengerArgumentNotSpecified, MessengerAlreadyExistsException {
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
    public void WHEN_try_to_create_existing_member_THEN_throw_exception() throws UserNotFoundException {
        initTestDatabaseByAddingUser();
        MemberDto createdMember = null;

        try {
            createdMember = createMember();
        } catch (UserNotFoundException | MemberAlreadyExistsException | GroupNotFoundException | MemberNotFoundException | MessengerAlreadyExistsException | MessengerArgumentNotSpecified e) {
            Assert.fail();
        }

        try {
            memberCrudFacade.add(createdMember);
            Assert.fail();
        } catch (MemberAlreadyExistsException e) {
            assertThat(e)
                    .isInstanceOf(MemberAlreadyExistsException.class)
                    .hasMessage(MemberCrudStatusEnum.MEMBER_ALREADY_EXIST.toString());
        } catch (MessengerAlreadyExistsException | MemberNotFoundException | GroupNotFoundException | MessengerArgumentNotSpecified e) {
            Assert.fail();
        }
    }

    @Test
    public void WHEN_find_existing_member_THEN_return_memberDto() throws UserNotFoundException, MemberAlreadyExistsException, MemberNotFoundException, GroupNotFoundException, MessengerArgumentNotSpecified, MessengerAlreadyExistsException {
        initTestDatabaseByAddingUser();
        UserDto foundUser = initUserCrudFacade().read(testUserDto);
        createMember();

        MemberDto foundMemberDto = memberCrudFacade.find(testMemberDto);

        assertThat(foundMemberDto).isNotNull();
        assertThat(foundMemberDto.getNick()).isEqualTo(testMemberDto.getNick());

        assertThat(foundMemberDto.getUser()).isEqualToComparingFieldByFieldRecursively(foundUser);
    }

    @Test
    public void WHEN_try_to_find_member_who_does_not_exist_THEN_return_MemberNotFoundException() throws UserNotFoundException {
        initTestDatabaseByAddingUser();

        try {
            memberCrudFacade.find(testMemberDto);
            Assert.fail();
        } catch (MemberNotFoundException e) {
            assertThat(e)
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage(MemberCrudStatusEnum.MEMBER_NOT_FOUND.toString());
        }
    }

    @Test
    public void WHEN_member_exists_THEN_return_true() throws UserNotFoundException, MemberAlreadyExistsException, MemberNotFoundException, GroupNotFoundException, MessengerAlreadyExistsException, MessengerArgumentNotSpecified {
        initTestDatabaseByAddingUser();
        createMember();

        boolean memberExists = memberCrudFacade.isExist(testMemberDto);
        assertThat(memberExists).isTrue();
    }

    @Test
    public void WHEN_member_does_not_exist_THEN_return_false() {
        boolean memberDoesNotExist = memberCrudFacade.isExist(testMemberDto);
        assertThat(memberDoesNotExist).isFalse();
    }

    @Test
    public void WHEN_try_to_update_existing_member_THEN_updated_member() throws UserNotFoundException, MemberAlreadyExistsException, MemberNotFoundException, MemberFoundButNotActiveException, GroupNotFoundException, MessengerArgumentNotSpecified, MessengerAlreadyExistsException {
        initTestDatabaseByAddingUser();
        UserDto foundUser = initUserCrudFacade().read(testUserDto);
        MemberDto createdMember = createMember();

        MemberDto updatedMember = memberCrudFacade.update(testMemberDto, updateTestMemberDto(testMemberDto));

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
    public void WHEN_try_to_update_existing_but_not_active_member_THEN_return_exception() throws MemberNotFoundException, UserNotFoundException, MemberAlreadyExistsException, MemberFoundButNotActiveException, GroupNotFoundException, MessengerArgumentNotSpecified, MessengerAlreadyExistsException, MessengerNotFoundException {
        initTestDatabaseByAddingUser();
        memberCrudFacade.add(testMemberDto);
        memberCrudFacade.delete(testMemberDto);

        try {
            memberCrudFacade.update(testMemberDto, updateTestMemberDto(testMemberDto));
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
            memberCrudFacade.update(testMemberDto, updateTestMemberDto(testMemberDto));
            Assert.fail();
        } catch (MemberNotFoundException e) {
            assertThat(e)
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage(MemberCrudStatusEnum.MEMBER_NOT_FOUND.toString());
        }
    }

    @Test
    public void WHEN_try_to_delete_existing_member_THEN_delete_member() throws MemberFoundButNotActiveException, MemberNotFoundException, UserNotFoundException, MemberAlreadyExistsException, GroupNotFoundException, MessengerArgumentNotSpecified, MessengerAlreadyExistsException, MessengerNotFoundException {
        initTestDatabaseByAddingUser();
        createMember();

        MemberDto isMemberDeleted = memberCrudFacade.delete(testMemberDto);
        assertThat(isMemberDeleted.isActive()).isFalse();
    }

    @Test
    public void WHEN_try_to_delete_non_existing_member_THEN_throw_EntityNotFoundException() throws UserNotFoundException, MemberFoundButNotActiveException, MessengerAlreadyExistsException, MessengerNotFoundException, GroupNotFoundException {
        initTestDatabaseByAddingUser();

        try {
            memberCrudFacade.delete(testMemberDto);
            Assert.fail();
        } catch (MemberNotFoundException e) {
            assertThat(e)
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage(MemberCrudStatusEnum.MEMBER_NOT_FOUND.toString());
        }
    }
}