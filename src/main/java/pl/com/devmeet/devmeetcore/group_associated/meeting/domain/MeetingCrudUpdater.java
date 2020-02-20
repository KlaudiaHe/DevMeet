package pl.com.devmeet.devmeetcore.group_associated.meeting.domain;

import org.joda.time.DateTime;
import pl.com.devmeet.devmeetcore.domain_utils.CrudEntityUpdater;
import pl.com.devmeet.devmeetcore.group_associated.meeting.domain.status_and_exceptions.MeetingNotFoundException;

public class MeetingCrudUpdater implements CrudEntityUpdater<MeetingDto, MeetingEntity> {

    private MeetingCrudFinder meetingCrudFinder;
    private MeetingCrudSaver meetingCrudSaver;

    public MeetingCrudUpdater(MeetingCrudRepository meetingCrudRepository) {
        this.meetingCrudSaver = new MeetingCrudSaver(meetingCrudRepository);
        this.meetingCrudFinder = new MeetingCrudFinder(meetingCrudRepository);
    }

    @Override
    public MeetingEntity updateEntity(MeetingDto update) throws MeetingNotFoundException {
        MeetingEntity found = meetingCrudFinder.findEntity(update);
        return meetingCrudSaver.saveEntity(
                updateAllParameters(found, mapToEntity(update))
        );
    }

    private MeetingEntity updateAllParameters(MeetingEntity oldMeetingEntity, MeetingEntity newMeetingEntity) {
        oldMeetingEntity.setMeetingNumber(oldMeetingEntity.getMeetingNumber());
        oldMeetingEntity.setPlace(newMeetingEntity.getPlace());
        oldMeetingEntity.setGroup(newMeetingEntity.getGroup());
        oldMeetingEntity.setActive(newMeetingEntity.isActive());
        oldMeetingEntity.setBeginTime(newMeetingEntity.getBeginTime());
        oldMeetingEntity.setCreationTime(newMeetingEntity.getCreationTime());
        oldMeetingEntity.setEndTime(newMeetingEntity.getEndTime());

        oldMeetingEntity.setModificationTime(DateTime.now());
        return oldMeetingEntity;
    }

    private MeetingEntity mapToEntity(MeetingDto meetingDto) {
        return MeetingMapper.map(meetingDto);
    }
}