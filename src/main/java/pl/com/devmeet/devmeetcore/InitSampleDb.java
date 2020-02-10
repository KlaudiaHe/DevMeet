package pl.com.devmeet.devmeetcore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.status_and_exceptions.GroupNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberCrudService;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberDto;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberAlreadyExistsException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberNotFoundException;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.status_and_exceptions.MemberUserNotActiveException;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.PlaceCrudService;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerAlreadyExistsException;
import pl.com.devmeet.devmeetcore.messenger_associated.messenger.status_and_exceptions.MessengerArgumentNotSpecified;
import pl.com.devmeet.devmeetcore.user.domain.UserDto;
import pl.com.devmeet.devmeetcore.user.domain.UserService;
import pl.com.devmeet.devmeetcore.user.domain.status_and_exceptions.UserNotFoundException;

@Component
class InitSampleDb implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private MemberCrudService memberService;

    @Autowired
    private PlaceCrudService placeService;

    @Override
    public void run(String... args) throws Exception {

        addUsers();
        addPlaces();


    }

    private void addPlaces() {

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

        System.out.println(placeService.findAll());

    }

    private void addUsers() throws MemberUserNotActiveException, GroupNotFoundException, MessengerArgumentNotSpecified, MemberAlreadyExistsException, MessengerAlreadyExistsException, UserNotFoundException, MemberNotFoundException {
        userService.add(UserDto.builder()
                .email("emailt407@gmail.com")
                .isActive(true)
                .build());

        memberService.add(MemberDto.builder()
                .user(userService.findByEmail("emailt407@gmail.com").get())
                .nick("admin")
                .isActive(true)
                .build());

        System.out.println(userService.findAll());
    }
}
