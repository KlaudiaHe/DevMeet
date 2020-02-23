package pl.com.devmeet.devmeetcore.place.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import pl.com.devmeet.devmeetcore.group_associated.meeting.domain.MeetingEntity;

import javax.persistence.*;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "places")
@Entity
//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PlaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//    private List<MemberEntity> members;

    private String placeName;

    private String description;

    private String website;

    private String location;

//    @OneToOne(mappedBy = "place", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
//    private AvailabilityEntity availability;

//    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
//    private List<PlaceVoteEntity> placeVotes;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MeetingEntity> meetings;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private DateTime creationTime;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private DateTime modificationTime;

    private boolean isActive;
}
