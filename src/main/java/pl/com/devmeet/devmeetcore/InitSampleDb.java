package pl.com.devmeet.devmeetcore;

import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupCrudService;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupDto;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.status_and_exceptions.GroupAlreadyExistsException;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.status_and_exceptions.GroupNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberCrudService;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberDto;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberRepository;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberAlreadyExistsException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberUserNotActiveException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerAlreadyExistsException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerArgumentNotSpecified;
import pl.com.devmeet.devmeetcore.place.domain.PlaceCrudService;
import pl.com.devmeet.devmeetcore.place.domain.PlaceDto;
import pl.com.devmeet.devmeetcore.place.domain.status_and_exceptions.PlaceAlreadyExistsException;
import pl.com.devmeet.devmeetcore.user.domain.UserCrudService;
import pl.com.devmeet.devmeetcore.user.domain.UserDto;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserAlreadyActiveException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserAlreadyExistsException;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

@Component
@AllArgsConstructor
public class InitSampleDb implements CommandLineRunner {

    private UserCrudService userService;
    private MemberRepository memberRepository;
    private MemberCrudService memberService;
    private PlaceCrudService placeService;
    private GroupCrudService groupService;

    @Override
    public void run(String... args) throws Exception {
        insertUsers();
        insertPlaces();
        initGroups();
    }

    private void insertUsers() throws MemberUserNotActiveException, GroupNotFoundException, MessengerArgumentNotSpecified, MemberAlreadyExistsException, MessengerAlreadyExistsException, UserNotFoundException, MemberNotFoundException, UserAlreadyExistsException, UserAlreadyActiveException {
        String userEmail = "adminforrunner@gmail.com";

        UserDto userDto = UserDto.builder()
                .email(userEmail)
                .build();

        UserDto userForAdminMember = userService.activation(userService.add(userDto));
        System.out.println(userService.findAll());

        MemberDto admin = MemberDto.builder()
                .user(userForAdminMember)
                .creationTime(DateTime.now())
                .nick("admin")
                .build();

        MemberDto createdMember = memberService.add(admin);
        System.out.println("Member created: " + createdMember.toString());
    }

    private void insertPlaces() throws MemberNotFoundException, UserNotFoundException, PlaceAlreadyExistsException {

        placeService.add(PlaceDto.builder()
                .placeName("Centrum Zarządzania Innowacjami i Transferem Technologii Politechniki Warszawskiej")
                .description("Openspace koło Metra Politechniki")
                .website("cziitt.pw.edu.pl")
                .location("Rektorska 4, 00-614 Warszawa")
                .build());

        placeService.add(PlaceDto.builder()
                .placeName("Google for Startups – Koneser")
                .description("Google Campus Warsaw")
                .website("https://www.campus.co/warsaw")
                .location("Plac Konesera 03-736 Warszawa (+48) 22 128 44 38")
                .build());

        placeService.add(PlaceDto.builder()
                .placeName("Wydział Matematyki, Informatyki i Mechaniki Uniwersytetu Warszawskiego – wydział Uniwersytetu Warszawskiego")
                .description("MeetUp tup! tup! tup! jeb!")
                .website("https://www.mimuw.edu.pl")
                .location("Stefana Banacha 2, 02-097 Warszawa")
                .build());

        placeService.add(PlaceDto.builder()
                .placeName("Centrum konferencyjne FOCUS")
                .description("budynek z drzewem na piętrze")
                .website("http://www.budynekfocus.com/pl")
                .location("Aleja Armii Ludowej 26, 00-609 Warszawa")
                .build());

        placeService.add(PlaceDto.builder()
                .placeName("Startberry")
                .description("We build bridges between #startups and #corporations Partners: EY, F11, Microsoft #MVP #hackathon #venturebuilder #vc #startberry")
                .website("https://twitter.com/startberrypl")
                .location("Grochowska 306/308, 03-840 Warszawa")
                .build());

        System.out.println(placeService.findAll());
    }


    private void initGroups() throws GroupAlreadyExistsException {
        groupService.add(GroupDto.builder()
                .groupName("Java test group")
                .website("www.testWebsite.com")
                .description("Welcome to test group")
                .build());

        groupService.add(GroupDto.builder()
                .groupName("DevMeet developers")
                .website("https://bitbucket.org/khturowska/devmeet/")
                .description("Awesome team")
                .build());
        System.out.println("Groups created: " + groupService.findAll());
    }
}
