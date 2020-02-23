package pl.com.devmeet.devmeetcore.place.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupCrudRepository;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.status_and_exceptions.GroupNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberCrudService;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberDto;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberEntity;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberRepository;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberAlreadyExistsException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberUserNotActiveException;
import pl.com.devmeet.devmeetcore.place.domain.status_and_exceptions.PlaceAlreadyExistsException;
import pl.com.devmeet.devmeetcore.place.domain.status_and_exceptions.PlaceCrudStatusEnum;
import pl.com.devmeet.devmeetcore.place.domain.status_and_exceptions.PlaceFoundButNotActiveException;
import pl.com.devmeet.devmeetcore.place.domain.status_and_exceptions.PlaceNotFoundException;
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
public class PlaceCrudServiceTest {

    @Autowired private PlaceCrudRepository repository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private MessengerRepository messengerRepository;
    @Autowired private GroupCrudRepository groupCrudRepository;

    private PlaceCrudService placeCrudService;
    private MemberCrudService memberCrudService;
    private UserCrudService userCrudService;

    private PlaceDto testPlaceDto1;
    private PlaceDto testPlaceDto2;
    private MemberDto testMemberDto;
    private UserDto testUserDto;

    @Before
    public void setUp() {

        testUserDto = UserDto.builder()
                .email("testplaceuser@gmail.com")
                .password("multiPass")
                .isActive(true)
                .build();

        testMemberDto = MemberDto.builder()
                .user(testUserDto)
                .nick("serpentyna123")
                .build();

        testPlaceDto1 = PlaceDto.builder()
                .placeName("FOCUS")
                .description("Centrum konferencyjne FOCUS - budynek z drzewem na piętrze")
                .website("http://www.budynekfocus.com/pl")
                .location("Aleja Armii Ludowej 26, 00-609 Warszawa")
                //    .availability(testAvailabilityDto)
//                .placeVotes(null)
                .creationTime(null)
                .modificationTime(null)
                .isActive(true)
                .build();

        testPlaceDto2 = PlaceDto.builder()
                .placeName("Wydział Matematyki, Informatyki i Mechaniki Uniwersytetu Warszawskiego – wydział Uniwersytetu Warszawskiego")
                .description("MeetUp tup! tup! tup! jeb!")
                .website("https://www.mimuw.edu.pl/")
                .location("Stefana Banacha 2, 02-097 Warszawa")
                //    .availability(testAvailabilityDto)
//                .placeVotes(null)
                .creationTime(null)
                .modificationTime(null)
                .isActive(true)
                .build();


    }

    private UserCrudService initUserCrudFacade() {
        return new UserCrudService(userRepository);
    }

    private MemberCrudService initMemberCrudFacade() {
        return new MemberCrudService(memberRepository, userRepository, messengerRepository, groupCrudRepository);
    }

    private PlaceCrudService initPlaceCrudFacade() {
        return new PlaceCrudService(repository, memberRepository, userRepository, messengerRepository, groupCrudRepository);
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

        return testUser != null &&
                memberEntity != null;
    }

    @Test
    public void USER_CRUD_FACADE_WR() throws UserNotFoundException, UserAlreadyExistsException {
        UserCrudService userCrudService = initUserCrudFacade();
        UserDto testUser = userCrudService.add(testUserDto);
        UserEntity userEntity = userCrudService.findEntity(testUser);
        assertThat(userEntity).isNotNull();
    }

    @Test
    public void MEMBER_CRUD_FACADE_WR() throws UserNotFoundException, MemberNotFoundException, UserAlreadyExistsException, UserAlreadyActiveException, MemberUserNotActiveException, MessengerArgumentNotSpecified, GroupNotFoundException, MessengerAlreadyExistsException, MemberAlreadyExistsException {
        MemberCrudService memberCrudService = initMemberCrudFacade();
        UserCrudService userCrudService = initUserCrudFacade();
        userCrudService.add(testUserDto);
        userCrudService.activation(testUserDto);
        memberCrudService.add(testMemberDto);
        MemberEntity memberEntity = memberCrudService.findEntityByUser(testUserDto);
        assertThat(memberEntity).isNotNull();
    }

    @Test
    public void INIT_TEST_DB() {
        boolean initDB = initTestDB();
        assertThat(initDB).isTrue();
    }

    @Test
    public void WHEN_try_to_create_non_existing_place_THEN_return_place() throws MemberNotFoundException, UserNotFoundException, PlaceAlreadyExistsException {
        initTestDB();
        placeCrudService = initPlaceCrudFacade();
        PlaceDto created = placeCrudService.add(testPlaceDto1);

//        assertThat(created.getMembers().getUser()).isNotNull();

        assertThat(created.getPlaceName()).isEqualTo(testPlaceDto1.getPlaceName());
        assertThat(created.getDescription()).isEqualTo(testPlaceDto1.getDescription());
        assertThat(created.getWebsite()).isEqualTo(testPlaceDto1.getWebsite());
        assertThat(created.getLocation()).isEqualTo(testPlaceDto1.getLocation());

        assertThat(created.getCreationTime()).isNotNull();
        assertThat(created.getModificationTime()).isNull();
        assertThat(created.isActive()).isTrue();
    }

    @Test
    public void WHEN_try_to_create_existing_place_THEN_EntityAlreadyExistsException() throws MemberNotFoundException, UserNotFoundException {
        initTestDB();
        placeCrudService = initPlaceCrudFacade();
        try {
            placeCrudService.add(testPlaceDto1);
        } catch (MemberNotFoundException | UserNotFoundException | PlaceAlreadyExistsException e) {
            Assert.fail();
        }
        try {
            placeCrudService.add(testPlaceDto1);
            Assert.fail();
        } catch (PlaceAlreadyExistsException e) {
            assertThat(e)
                    .isInstanceOf(PlaceAlreadyExistsException.class)
                    .hasMessage(PlaceCrudStatusEnum.PLACE_ALREADY_EXISTS.toString());
        }
    }

    @Test
    public void WHEN_found_place_THEN_return_place() throws MemberNotFoundException, UserNotFoundException, PlaceAlreadyExistsException, PlaceNotFoundException {
        initTestDB();
        PlaceDto found;
        placeCrudService = initPlaceCrudFacade();
        PlaceDto created = placeCrudService.add(testPlaceDto1);

        found = placeCrudService.findPlaceByIdAndMapToDto(created.getId());
        assertThat(found).isNotNull();
    }

    @Test
    public void WHEN_try_to_find_non_existing_place_THEN_return_EntityNotFoundException() throws MemberNotFoundException, UserNotFoundException {
        initTestDB();
        placeCrudService = initPlaceCrudFacade();
        try {
            placeCrudService.findPlaceByIdOrFeaturesAndMapToDto(testPlaceDto1);
            Assert.fail();
        } catch (PlaceNotFoundException e) {
            assertThat(e)
                    .isInstanceOf(PlaceNotFoundException.class)
                    .hasMessage(PlaceCrudStatusEnum.PLACE_NOT_FOUND.toString());
        }
    }

    @Test
    public void WHEN_try_to_find_all_places_THEN_return_places() throws MemberNotFoundException, UserNotFoundException, PlaceAlreadyExistsException {
        initTestDB();
        List<PlaceDto> found;
        PlaceCrudService placeCrudService = initPlaceCrudFacade();
        placeCrudService.add(testPlaceDto1);
        placeCrudService.add(testPlaceDto2);
        found = placeCrudService.findAll();

        assertThat(found.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void WHEN_try_to_update_existing_place_THEN_return_place() throws MemberNotFoundException, UserNotFoundException, PlaceAlreadyExistsException, PlaceNotFoundException {
        initTestDB();
        PlaceCrudService placeCrudService = initPlaceCrudFacade();
        PlaceDto created = placeCrudService.add(testPlaceDto1);
        PlaceDto updated = placeCrudService.update(placeUpdatedValues(testPlaceDto1));
//        assertThat(updated.getMember()).isEqualToComparingFieldByField(created.getMember());
        assertThat(updated.getPlaceName()).isEqualTo(created.getPlaceName());
        assertThat(updated.getDescription()).isNotEqualTo(created.getDescription());
        assertThat(updated.getWebsite()).isNotEqualTo(created.getWebsite());
        assertThat(updated.getWebsite()).isEqualTo("www.pw.pl");
        assertThat(updated.getDescription()).isEqualTo("openspace");
        assertThat(updated.getLocation()).isEqualTo(created.getLocation());
        //   assertThat(updated.getAvailability()).isEqualTo(created.getAvailability());
//        assertThat(updated.getPlaceVotes()).isEqualTo(created.getPlaceVotes());
        assertThat(updated.getCreationTime()).isEqualTo(created.getCreationTime());
        assertThat(updated.getModificationTime()).isNotEqualTo(created.getModificationTime());
        assertThat(updated.isActive()).isEqualTo(created.isActive());
    }

    private PlaceDto placeUpdatedValues(PlaceDto testPlaceDto) {
        testPlaceDto.setWebsite("www.pw.pl");
        testPlaceDto.setDescription("openspace");
        return testPlaceDto;
    }

    @Test
    public void WHEN_try_to_update_non_existing_place_THEN_return_EntityNotFoundException() throws MemberNotFoundException, UserNotFoundException {
        initTestDB();
        PlaceCrudService placeCrudService = initPlaceCrudFacade();
        try {
            placeCrudService.update(placeUpdatedValues(testPlaceDto1));
        } catch (PlaceNotFoundException e) {
            assertThat(e)
                    .isInstanceOf(PlaceNotFoundException.class)
                    .hasMessage(PlaceCrudStatusEnum.PLACE_NOT_FOUND.toString());
        }
    }

    @Test
    public void WHEN_delete_existing_place_THEN_return_place() throws MemberNotFoundException, UserNotFoundException, PlaceAlreadyExistsException, PlaceNotFoundException, PlaceFoundButNotActiveException {
        initTestDB();
        PlaceCrudService placeCrudService = initPlaceCrudFacade();
        PlaceDto created = placeCrudService.add(testPlaceDto1);
        PlaceDto deleted = placeCrudService.delete(testPlaceDto1);

        assertThat(deleted).isNotNull();
        assertThat(deleted.isActive()).isNotEqualTo(created.isActive());
        assertThat(deleted.getCreationTime()).isEqualTo(created.getCreationTime());
        assertThat(deleted.getModificationTime()).isNotNull();
    }

    @Test
    public void WHEN_try_to_delete_non_existing_place_THEN_return_EntityNotFoundException() throws UserNotFoundException, MemberNotFoundException, PlaceFoundButNotActiveException {
        initTestDB();
        PlaceCrudService placeCrudService = initPlaceCrudFacade();

        try {
            placeCrudService.delete(testPlaceDto1);
        } catch (PlaceNotFoundException e) {
            assertThat(e)
                    .isInstanceOf(PlaceNotFoundException.class)
                    .hasMessage(PlaceCrudStatusEnum.PLACE_NOT_FOUND.toString());
        }
    }
}