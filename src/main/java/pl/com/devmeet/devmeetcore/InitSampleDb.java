package pl.com.devmeet.devmeetcore;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.status_and_exceptions.GroupNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberCrudService;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberAlreadyExistsException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberUserNotActiveException;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.PlaceCrudService;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.status_and_exceptions.PlaceAlreadyExistsException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerAlreadyExistsException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerArgumentNotSpecified;
import pl.com.devmeet.devmeetcore.user.domain.UserDto;
import pl.com.devmeet.devmeetcore.user.domain.UserService;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

@Component
@AllArgsConstructor
public class InitSampleDb implements CommandLineRunner {

    private UserService userService;
    private MemberCrudService memberService;
    private PlaceCrudService placeService;
//    private MemberRepository memberRepository;
//    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        insertUsers();
        insertPlaces();
    }

    private void insertPlaces() throws MemberNotFoundException, UserNotFoundException, PlaceAlreadyExistsException {

//        placeService.add(PlaceDto.builder()
//                .member(memberService.findById(1L).get())
//                .placeName("Centrum Zarządzania Innowacjami i Transferem Technologii Politechniki Warszawskiej")
//                .description("Openspace koło Metra Politechniki")
//                .website("cziitt.pw.edu.pl")
//                .location("Rektorska 4, 00-614 Warszawa")
//                .build());

//        placeService.add(PlaceDto.builder()
//                .placeName("Google for Startups – Koneser")
//                .description("Google Campus Warsaw")
//                .website("https://www.campus.co/warsaw")
//                .location("Plac Konesera 03-736 Warszawa (+48) 22 128 44 38")
//                .build());
//
//        placeService.add(PlaceDto.builder()
//                .placeName("Wydział Matematyki, Informatyki i Mechaniki Uniwersytetu Warszawskiego – wydział Uniwersytetu Warszawskiego")
//                .description("MeetUp tup! tup! tup! jeb!")
//                .website("https://www.mimuw.edu.pl")
//                .location("Stefana Banacha 2, 02-097 Warszawa")
//                .build());
//
//        placeService.add(PlaceDto.builder()
//                .placeName("Centrum konferencyjne FOCUS")
//                .description("budynek z drzewem na piętrze")
//                .website("http://www.budynekfocus.com/pl")
//                .location("Aleja Armii Ludowej 26, 00-609 Warszawa")
//                .build());

//        placeService.add(PlaceDto.builder()
//                .placeName("")
//                .description("")
//                .website("")
//                .location("")
//                .build());

//        System.out.println(placeService.findAll());

    }

    private void insertUsers() throws MemberUserNotActiveException, GroupNotFoundException, MessengerArgumentNotSpecified, MemberAlreadyExistsException, MessengerAlreadyExistsException, UserNotFoundException, MemberNotFoundException {

        UserDto user1 = UserDto.builder()
                .email("emailt407@gmail.com")
                .isActive(true)
                .build();
        userService.add(user1);
        System.out.println(userService.findAll());

//        memberService.add(MemberDto.builder()
//                .user(user1)
//                .creationTime(DateTime.now())
//                .nick("admin")
//                .isActive(true)
//                .build());

    }
}
