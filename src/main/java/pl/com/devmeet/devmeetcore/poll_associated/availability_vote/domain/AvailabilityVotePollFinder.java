package pl.com.devmeet.devmeetcore.poll_associated.availability_vote.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.status_and_exceptions.GroupNotFoundException;
import pl.com.devmeet.devmeetcore.poll_associated.poll.domain.PollCrudService;
import pl.com.devmeet.devmeetcore.poll_associated.poll.domain.PollDto;
import pl.com.devmeet.devmeetcore.poll_associated.poll.domain.PollEntity;
import pl.com.devmeet.devmeetcore.poll_associated.poll.domain.status_and_exceptions.PollNotFoundException;

@AllArgsConstructor
@NoArgsConstructor
@Builder
class AvailabilityVotePollFinder {

    private PollCrudService pollCrudService;

    public PollEntity findPoll(PollDto dto) throws GroupNotFoundException, PollNotFoundException {
            return pollCrudService.findEntity(dto);
    }
}
