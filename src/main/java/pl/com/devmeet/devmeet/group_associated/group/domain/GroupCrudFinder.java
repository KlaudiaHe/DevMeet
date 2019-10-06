package pl.com.devmeet.devmeet.group_associated.group.domain;

import pl.com.devmeet.devmeet.domain_utils.CrudEntityFinder;
import pl.com.devmeet.devmeet.group_associated.group.domain.status.GroupCrudInfoStatusEnum;

import java.util.List;
import java.util.Optional;

class GroupCrudFinder implements CrudEntityFinder<GroupDto, GroupEntity> {

    private GroupCrudSaver groupCrudSaver;
    private GroupCrudRepository repository;

    public GroupCrudFinder(GroupCrudRepository repository) {
        this.groupCrudSaver = new GroupCrudSaver(repository);
        this.repository = repository;
    }

    @Override
    public GroupEntity findEntity(GroupDto dto) {
        Optional<GroupEntity> group = findGroup(dto);

        if (group.isPresent())
            return group.get();
        else
            throw new IllegalArgumentException(GroupCrudInfoStatusEnum.GROUP_NOT_FOUND.toString());
    }

    private Optional<GroupEntity> findGroup(GroupDto dto) {
        String groupName = dto.getGroupName();

        if (checkGroupName(groupName))
            return repository.findByGroupName(groupName);

        return Optional.empty();
    }

    @Override
    public List<GroupEntity> findEntities(GroupDto dto) {
        Optional<List<GroupEntity>> groups;

        String groupName = dto.getGroupName();

        if (checkGroupName(groupName)) {
            groups = repository.findAllByGroupName(groupName);

            if (groups.isPresent())
                return groups.get();
        }

        throw new IllegalArgumentException(GroupCrudInfoStatusEnum.GROUPS_NOT_FOUND.toString());
    }

    @Override
    public boolean isExist(GroupDto dto) {
        return findGroup(dto).isPresent();
    }

    private boolean checkGroupName(String groupName) {
        return groupName != null && !groupName.equals("");
    }
}
