package pl.com.devmeet.devmeet.poll_associated.availability_vote.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.com.devmeet.devmeet.domain_utils.CrudEntitySaver;
import pl.com.devmeet.devmeet.domain_utils.EntityNotFoundException;
import pl.com.devmeet.devmeet.member_associated.member.domain.MemberCrudFacade;
import pl.com.devmeet.devmeet.member_associated.member.domain.MemberEntity;
import pl.com.devmeet.devmeet.poll_associated.poll.domain.PollCrudFacade;
import pl.com.devmeet.devmeet.poll_associated.poll.domain.PollEntity;

@Builder
@AllArgsConstructor
@NoArgsConstructor
class AvailabilityVoteCrudSaver implements CrudEntitySaver<AvailabilityVoteEntity, AvailabilityVoteEntity> {

    private AvailabilityVoteCrudRepository availabilityVoteCrudRepository;
    private AvailabilityVotePollFinder pollFinder;
    private AvailabilityVoteMemberFinder memberFinder;

    @Override
    public AvailabilityVoteEntity saveEntity(AvailabilityVoteEntity entity) throws EntityNotFoundException {
        return availabilityVoteCrudRepository.save(connectVoteWithMember(connectVoteWithPoll(entity)));
    }

    private AvailabilityVoteEntity connectVoteWithPoll(AvailabilityVoteEntity voteEntity){
        PollEntity pollEntity = voteEntity.getPoll();

        if(pollEntity.getId() == null)
            pollEntity = pollFinder.findPoll(PollCrudFacade.map(pollEntity));

        voteEntity.setPoll(pollEntity);
        return voteEntity;
    }

    private AvailabilityVoteEntity connectVoteWithMember(AvailabilityVoteEntity voteEntity){
        MemberEntity memberEntity = voteEntity.getMember();

        if(memberEntity.getId() == null)
            memberEntity = memberFinder.findMember(MemberCrudFacade.map(memberEntity));

        voteEntity.setMember(memberEntity);
        return voteEntity;
    }
}
