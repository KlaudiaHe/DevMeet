package pl.com.devmeet.devmeetcore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.PlaceCrudService;
import pl.com.devmeet.devmeetcore.user.domain.UserDto;
import pl.com.devmeet.devmeetcore.user.domain.UserService;

@Component
class InitSampleDb implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private PlaceCrudService placeService;

    @Override
    public void run(String... args) throws Exception {

        addUsers();
        addPlaces();


    }

    private void addPlaces() {

//        placeService.add(PlaceDto.builder()
//                .member(MemberDto.builder().build())
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

    private void addUsers() {
        userService.add(UserDto.builder().email("emailt407@gmail.com").build());
        System.out.println(userService.findAll());
    }
}
