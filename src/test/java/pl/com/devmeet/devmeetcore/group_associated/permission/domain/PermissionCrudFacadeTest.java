package pl.com.devmeet.devmeetcore.group_associated.permission.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupCrudFacade;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupCrudRepository;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupDto;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupEntity;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.status_and_exceptions.GroupAlreadyExistsException;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.status_and_exceptions.GroupNotFoundException;
import pl.com.devmeet.devmeetcore.group_associated.permission.domain.status_and_exceptions.*;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberCrudFacade;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberDto;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberEntity;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberRepository;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberAlreadyExistsException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.domain.MessengerRepository;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerAlreadyExistsException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerArgumentNotSpecified;
import pl.com.devmeet.devmeetcore.user.domain.*;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class PermissionCrudFacadeTest {

    @Autowired
    private PermissionCrudRepository permissionCrudRepository;
    @Autowired
    private GroupCrudRepository groupCrudRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessengerRepository messengerRepository;

    private PermissionCrudFacade permissionCrudFacade;
    private GroupCrudFacade groupCrudFacade;
    private MemberCrudFacade memberCrudFacade;
    private UserCrudFacade userCrudFacade;

    private GroupDto testGroupDto;
    private MemberDto testMemberDto;
    private UserDto testUserDto;
    private PermissionDto testPermissionDto;

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
                .nick("WasatyJanusz")
                .build();

        testGroupDto = GroupDto.builder()
                .groupName("Java test group")
                .website("www.testWebsite.com")
                .description("Welcome to test group")
                .membersLimit(5)
                .memberCounter(6)
                .meetingCounter(1)
                .creationTime(null)
                .modificationTime(null)
                .isActive(false)
                .build();

        testPermissionDto = PermissionDto.builder()
                .member(testMemberDto)
                .group(testGroupDto)
                .possibleToVote(true)
                .possibleToMessaging(true)
                .possibleToChangeGroupName(true)
                .possibleToBanMember(true)
                .memberBaned(false)
                .isActive(false)
                .build();
    }

    private UserCrudFacade initUserCrudFacade() {
        return new UserCrudFacade(userRepository);
    }

    private GroupCrudFacade initGroupCrudFacade() {
        return new GroupCrudFacade(groupCrudRepository, memberRepository, userRepository, messengerRepository);
    }

    private MemberCrudFacade initMemberCrudFacade() {
        return new MemberCrudFacade(memberRepository, userRepository, messengerRepository, groupCrudRepository);
    }

    private PermissionCrudFacade initPermissionCrudFacade() {
        return new PermissionCrudFacade(permissionCrudRepository, groupCrudRepository, memberRepository, userRepository, messengerRepository);
    }

    private boolean initTestDB() {
        userCrudFacade = initUserCrudFacade();
        groupCrudFacade = initGroupCrudFacade();
        memberCrudFacade = initMemberCrudFacade();

        UserEntity testUser = userCrudFacade
                .findEntity(userCrudFacade
                        .create(testUserDto, DefaultUserLoginTypeEnum.PHONE));

        GroupEntity groupEntity = null;
        try {
            groupEntity = groupCrudFacade
                    .findEntityByGroup(groupCrudFacade
                            .add(testGroupDto));
        } catch (GroupNotFoundException | GroupAlreadyExistsException | UserNotFoundException | MemberNotFoundException | MessengerAlreadyExistsException | MessengerArgumentNotSpecified e) {
            e.printStackTrace();
        }


        MemberEntity memberEntity = null;
        try {
            memberEntity = memberCrudFacade
                    .findEntity(memberCrudFacade
                            .add(testMemberDto));
        } catch (MemberNotFoundException | MemberAlreadyExistsException | UserNotFoundException | GroupNotFoundException | MessengerAlreadyExistsException | MessengerArgumentNotSpecified e) {
            e.printStackTrace();
        }

        return testUser != null
                && groupEntity != null
                && memberEntity != null;
    }

    @Test
    public void USER_CRUD_FACADE_WR() {
        UserCrudFacade userFacade = initUserCrudFacade();
        UserDto testUser = userFacade.create(testUserDto, DefaultUserLoginTypeEnum.PHONE);
        UserEntity userEntity = userFacade.findEntity(testUser);

        assertThat(userEntity).isNotNull();
    }

    @Test
    public void GROUP_CRUD_FACADE_WR() throws GroupAlreadyExistsException, GroupNotFoundException, UserNotFoundException, MessengerArgumentNotSpecified, MemberNotFoundException, MessengerAlreadyExistsException {
        GroupCrudFacade groupFacade = initGroupCrudFacade();
        GroupEntity groupEntity = groupFacade.findEntityByGroup(groupFacade.add(testGroupDto));

        assertThat(groupEntity).isNotNull();
    }

    @Test
    public void MEMBER_CRUD_FACADE_WR() throws UserNotFoundException, MemberAlreadyExistsException, MemberNotFoundException, GroupNotFoundException, MessengerArgumentNotSpecified, MessengerAlreadyExistsException {
        MemberCrudFacade memberFacade = initMemberCrudFacade();
        initUserCrudFacade().create(testUserDto, DefaultUserLoginTypeEnum.EMAIL);
        MemberEntity memberEntity = memberFacade.findEntity(memberFacade.add(testMemberDto));

        assertThat(memberEntity).isNotNull();
    }

    @Test
    public void INIT_TEST_DB() {
        boolean initDB = initTestDB();
        assertThat(initDB).isTrue();
    }

    @Test
    public void WHEN_create_not_existing_permission_to_group_THEN_return_permission() throws UserNotFoundException, GroupNotFoundException, PermissionAlreadyExistsException, MemberNotFoundException {
        PermissionDto created;
        initTestDB();

        permissionCrudFacade = initPermissionCrudFacade();

        created = permissionCrudFacade.add(testPermissionDto);

        assertThat(created.getMember()).isNotNull();
        assertThat(created.getGroup()).isNotNull();

        assertThat(created.isPossibleToVote()).isTrue();
        assertThat(created.isPossibleToMessaging()).isTrue();
        assertThat(created.isPossibleToChangeGroupName()).isTrue();
        assertThat(created.isPossibleToBanMember()).isTrue();

        assertThat(created.getCreationTime()).isNotNull();
        assertThat(created.getModificationTime()).isNull();
        assertThat(created.isActive()).isTrue();
    }

    @Test
    public void WHEN_try_to_create_existing_permission_to_group_THEN_EntityAlreadyExistsException() throws UserNotFoundException, MemberNotFoundException, GroupNotFoundException {
        initTestDB();
        permissionCrudFacade = initPermissionCrudFacade();
        try {
            permissionCrudFacade.add(testPermissionDto);
        } catch (UserNotFoundException | MemberNotFoundException | PermissionAlreadyExistsException | GroupNotFoundException e) {
            Assert.fail();
        }

        try {
            permissionCrudFacade.add(testPermissionDto);
            Assert.fail();
        } catch (PermissionAlreadyExistsException e) {
            assertThat(e)
                    .isInstanceOf(PermissionAlreadyExistsException.class)
                    .hasMessage(PermissionCrudStatusEnum.PERMISSION_ALREADY_EXISTS.toString());
        }
    }

    @Test
    public void WHEN_found_permission_THEN_return_permission() throws UserNotFoundException, GroupNotFoundException, PermissionAlreadyExistsException, MemberNotFoundException, PermissionNotFoundException {
        initTestDB();
        PermissionDto created;
        PermissionDto found = null;
        permissionCrudFacade = initPermissionCrudFacade();

        created = permissionCrudFacade.add(testPermissionDto);

        found = permissionCrudFacade.find(testPermissionDto);


        assertThat(found).isNotNull();
    }

    @Test
    public void WHEN_try_to_find_not_existing_permission_THEN_return_EntityNotFoundException() throws UserNotFoundException, MemberNotFoundException, GroupNotFoundException {
        initTestDB();
        permissionCrudFacade = initPermissionCrudFacade();
        try {
            permissionCrudFacade.find(testPermissionDto);
            Assert.fail();
        } catch (PermissionNotFoundException e) {
            assertThat(e)
                    .isInstanceOf(PermissionNotFoundException.class)
                    .hasMessage(PermissionCrudStatusEnum.PERMISSION_NOT_FOUND.toString());
        }
    }

    @Test
    public void WHEN_try_to_find_all_permissions_THEN_return_UnsupportedOperationException() {
        PermissionCrudFacade permissionCrudFacade = initPermissionCrudFacade();
        try {
            permissionCrudFacade.findAll(testPermissionDto);
        } catch (PermissionMethodNotImplemented e) {
            assertThat(e)
                    .isInstanceOf(PermissionMethodNotImplemented.class)
                    .hasMessage(PermissionCrudStatusEnum.METHOD_NOT_IMPLEMENTED.toString());
        }
    }

    @Test
    public void WHEN_update_existing_permission_THEN_return_permission() throws UserNotFoundException, GroupNotFoundException, PermissionAlreadyExistsException, MemberNotFoundException, PermissionNotFoundException, PermissionException {
        initTestDB();
        PermissionCrudFacade permissionCrudFacade = initPermissionCrudFacade();
        PermissionDto created = permissionCrudFacade.add(testPermissionDto);
        PermissionDto updated = permissionCrudFacade.update(testPermissionDto, permissionUpdatedValues(testPermissionDto));

        assertThat(updated.isPossibleToVote()).isFalse();
        assertThat(updated.isPossibleToMessaging()).isFalse();
        assertThat(updated.isPossibleToChangeGroupName()).isFalse();
        assertThat(updated.isPossibleToBanMember()).isFalse();

        assertThat(updated.isMemberBaned()).isTrue();

        assertThat(updated.getMember()).isEqualToComparingFieldByField(created.getMember());
        assertThat(updated.getGroup()).isEqualToComparingFieldByField(created.getGroup());
        assertThat(updated.getCreationTime()).isEqualTo(created.getCreationTime());
        assertThat(updated.getModificationTime()).isNotEqualTo(created.getModificationTime());
        assertThat(updated.isActive()).isEqualTo(created.isActive());
    }

    private PermissionDto permissionUpdatedValues(PermissionDto permission) {
        permission.setPossibleToVote(false);
        permission.setPossibleToMessaging(false);
        permission.setPossibleToChangeGroupName(false);
        permission.setPossibleToBanMember(false);

        permission.setMemberBaned(true);
        return permission;
    }

    @Test
    public void WHEN_try_to_update_not_existing_permission_THEN_return_EntityNotFoundException() throws UserNotFoundException, GroupNotFoundException, MemberNotFoundException, PermissionException {
        initTestDB();
        PermissionCrudFacade permissionCrudFacade = initPermissionCrudFacade();
        try {
            permissionCrudFacade.update(testPermissionDto, permissionUpdatedValues(testPermissionDto));
        } catch (PermissionNotFoundException e) {
            assertThat(e)
                    .isInstanceOf(PermissionNotFoundException.class)
                    .hasMessage(PermissionCrudStatusEnum.PERMISSION_NOT_FOUND.toString());
        }
    }

    @Test
    public void WHEN_delete_existing_permission_THEN_return_permission() throws UserNotFoundException, GroupNotFoundException, PermissionAlreadyExistsException, MemberNotFoundException, PermissionNotFoundException {
        initTestDB();
        PermissionCrudFacade permissionCrudFacade = initPermissionCrudFacade();
        PermissionDto created = permissionCrudFacade.add(testPermissionDto);
        PermissionDto deleted = permissionCrudFacade.delete(testPermissionDto);

        assertThat(deleted).isNotNull();
        assertThat(deleted.isActive()).isNotEqualTo(created.isActive());
        assertThat(deleted.getCreationTime()).isEqualTo(created.getCreationTime());
        assertThat(deleted.getModificationTime()).isNotNull();
    }

    @Test
    public void WHEN_delete_not_existing_permission_THEN_return_EntityNotFoundException() throws UserNotFoundException, GroupNotFoundException, PermissionAlreadyExistsException, MemberNotFoundException {
        initTestDB();
        PermissionCrudFacade permissionCrudFacade = initPermissionCrudFacade();

        try {
            permissionCrudFacade.delete(testPermissionDto);
        } catch (PermissionNotFoundException e) {
            assertThat(e)
                    .isInstanceOf(PermissionNotFoundException.class)
                    .hasMessage(PermissionCrudStatusEnum.PERMISSION_NOT_FOUND.toString());
        }
    }
}