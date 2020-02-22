package pl.com.devmeet.devmeetcore.poll_associated.poll.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.devmeet.devmeetcore.domain_utils.CrudErrorEnum;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupCrudService;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupCrudRepository;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupDto;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupEntity;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.status_and_exceptions.GroupAlreadyExistsException;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.status_and_exceptions.GroupNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberRepository;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.domain.MessengerRepository;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerAlreadyExistsException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerArgumentNotSpecified;
import pl.com.devmeet.devmeetcore.poll_associated.poll.domain.status_and_exceptions.PollAlreadyExistsException;
import pl.com.devmeet.devmeetcore.poll_associated.poll.domain.status_and_exceptions.PollCrudStatusEnum;
import pl.com.devmeet.devmeetcore.poll_associated.poll.domain.status_and_exceptions.PollNotFoundException;
import pl.com.devmeet.devmeetcore.poll_associated.poll.domain.status_and_exceptions.PollUnsupportedOperationException;
import pl.com.devmeet.devmeetcore.user.domain.UserRepository;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
@DataJpaTest
@RunWith(SpringRunner.class)
public class PollCrudServiceTest {

    @Autowired
    private PollCrudRepository pollCrudRepository;

    @Autowired
    private GroupCrudRepository groupCrudRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessengerRepository messengerRepository;

    private PollCrudService pollCrudService;
    private GroupCrudService groupCrudService;

    private PollDto testPollDto;
    private GroupDto testGroupDto;

    @Before
    public void setUp() {

        testGroupDto = new GroupDto().builder()
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

        testPollDto = new PollDto().builder()
                .group(testGroupDto)
                .placeVotes(null)
                .availabilityVotes(null)
                .creationTime(null)
                .active(true)
                .build();
    }

    private GroupCrudService initGroupCrudFacade() {
        return new GroupCrudService(groupCrudRepository, memberRepository, userRepository, messengerRepository);
    }

    private PollCrudService initPollCrudFacade() {
        return new PollCrudService(pollCrudRepository, groupCrudRepository, memberRepository, userRepository, messengerRepository);
    }

    private boolean initTestDB() {
        groupCrudService = initGroupCrudFacade();

        GroupEntity groupEntity = null;
        try {
            groupEntity = groupCrudService
                    .findEntityByGroup(groupCrudService
                            .add(testGroupDto));
        } catch (GroupNotFoundException | GroupAlreadyExistsException | UserNotFoundException | MemberNotFoundException | MessengerAlreadyExistsException | MessengerArgumentNotSpecified e) {
            e.printStackTrace();
        }

        return groupEntity != null;
    }

    @Test
    public void INIT_TEST_DB() {
        boolean initDB = initTestDB();
        assertThat(initDB).isTrue();
    }

    @Test
    public void WHEN_create_not_existing_poll_THEN_return_poll() throws PollAlreadyExistsException, GroupNotFoundException {
        initTestDB();
        PollDto pollDto = initPollCrudFacade().add(testPollDto);

        assertThat(pollDto).isNotNull();
        assertThat(pollDto.getGroup()).isNotNull();
        assertThat(pollDto.getCreationTime()).isNotNull();
        assertThat(pollDto.isActive()).isTrue();
    }

    @Test
    public void WHEN_try_to_create_existing_poll_THEN_return_EntityAlreadyExistsException() throws GroupNotFoundException {
        initTestDB();
        PollCrudService pollCrudService = initPollCrudFacade();
        try {
            pollCrudService.add(testPollDto);
        } catch (PollAlreadyExistsException | GroupNotFoundException e) {
            Assert.fail();
        }

        try {
            pollCrudService.add(testPollDto);
            Assert.fail();
        } catch (PollAlreadyExistsException e) {
            assertThat(e)
                    .isInstanceOf(PollAlreadyExistsException.class)
                    .hasMessage(PollCrudStatusEnum.POLL_ALREADY_EXISTS.toString());
        }
    }

    @Test
    public void WHEN_found_active_poll_THEN_return_poll() throws PollAlreadyExistsException, GroupNotFoundException, PollNotFoundException {
        initTestDB();
        PollCrudService pollCrudService = initPollCrudFacade();
        PollDto pollDto = pollCrudService.add(testPollDto);
        PollDto found = pollCrudService.find(pollDto);

        assertThat(found).isNotNull();
        assertThat(found.isActive()).isTrue();
    }

    @Test
    public void WHEN_try_to_find_not_existing_poll_THEN_return_EntityNotFoundException() throws GroupNotFoundException {
        initTestDB();
        try {
            initPollCrudFacade().find(testPollDto);
            Assert.fail();
        } catch (PollNotFoundException e) {
            assertThat(e)
                    .isInstanceOf(PollNotFoundException.class)
                    .hasMessage(PollCrudStatusEnum.POLL_NOT_FOUND.toString());
        }
    }

    @Test
    public void readAll_not_support() {
        PollCrudService pollCrudService = initPollCrudFacade();
        try {
            pollCrudService.update(testPollDto, testPollDto);
            Assert.fail();
        } catch (PollUnsupportedOperationException e) {
            assertThat(e)
                    .isInstanceOf(PollUnsupportedOperationException.class)
                    .hasMessage(CrudErrorEnum.METHOD_NOT_IMPLEMENTED.toString());
        }
    }


    @Test
    public void update_not_support() {
        PollCrudService pollCrudService = initPollCrudFacade();
        try {
            pollCrudService.update(testPollDto, testPollDto);
            Assert.fail();
        } catch (PollUnsupportedOperationException e) {
            assertThat(e)
                    .isInstanceOf(PollUnsupportedOperationException.class)
                    .hasMessage(CrudErrorEnum.METHOD_NOT_IMPLEMENTED.toString());
        }
    }

    @Test
    public void WHEN_delete_active_poll_THEN_return_poll() throws PollAlreadyExistsException, GroupNotFoundException, PollNotFoundException {
        initTestDB();
        PollCrudService pollCrudService = initPollCrudFacade();
        pollCrudService.add(testPollDto);
        PollDto deleted = pollCrudService.delete(testPollDto);

        assertThat(deleted).isNotNull();
        assertThat(deleted.isActive()).isFalse();
    }

    @Test
    public void WHEN_delete_not_active_poll_THEN_return_EntityAlreadyExistsException() throws GroupNotFoundException {
        initTestDB();
        PollCrudService pollCrudService = initPollCrudFacade();

        try {
            pollCrudService.add(testPollDto);
        } catch (PollAlreadyExistsException e) {
            Assert.fail();
        }

        try {
            pollCrudService.delete(testPollDto);
        } catch (PollNotFoundException | PollAlreadyExistsException e) {
            Assert.fail();
        }

        try {
            initPollCrudFacade().delete(testPollDto);
        } catch (PollNotFoundException | PollAlreadyExistsException e) {
            assertThat(e)
                    .isInstanceOf(PollAlreadyExistsException.class)
                    .hasMessage(PollCrudStatusEnum.POLL_FOUND_BUT_NOT_ACTIVE.toString());
        }
    }
}