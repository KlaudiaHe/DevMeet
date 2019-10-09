package pl.com.devmeet.devmeet.group_associated.group.domain;

import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import pl.com.devmeet.devmeet.domain_utils.CrudEntityCreator;
import pl.com.devmeet.devmeet.group_associated.group.domain.status.GroupCrudInfoStatusEnum;

@AllArgsConstructor
class GroupCrudCreator implements CrudEntityCreator<GroupDto, GroupEntity> {

    private GroupCrudSaver saver;
    private GroupCrudFinder finder;

    public GroupCrudCreator(GroupCrudRepository repository) {
        this.saver = new GroupCrudSaver(repository);
        this.finder = new GroupCrudFinder(repository);
    }

    @Override
    public GroupEntity createEntity(GroupDto dto) {
        GroupEntity group;
        boolean groupActivity;

        try {
            group = finder.findEntity(dto);
            groupActivity = group.isActive();

            if (!groupActivity && group.getModificationTime() != null)
                return saver.saveEntity(setDefaultValuesWhenGroupExists(group));
            else
                throw new IllegalArgumentException(GroupCrudInfoStatusEnum.GROUP_ALREADY_EXISTS.toString());

        } catch (IllegalArgumentException e) {
            return saver.saveEntity(setDefaultValuesWhenGroupNotExists(GroupCrudFacade.map(dto)));
        }
    }


    private GroupEntity setDefaultValuesWhenGroupNotExists(GroupEntity group) {
        group.setCreationTime(DateTime.now());
        group.setActive(true);
        return group;
    }

    private GroupEntity setDefaultValuesWhenGroupExists(GroupEntity entity) {
        entity.setModificationTime(DateTime.now());
        entity.setActive(true);
        return entity;
    }
}
