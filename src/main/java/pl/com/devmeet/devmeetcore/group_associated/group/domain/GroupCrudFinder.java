package pl.com.devmeet.devmeetcore.group_associated.group.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.status_and_exceptions.GroupCrudStatusEnum;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.status_and_exceptions.GroupNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Builder
class GroupCrudFinder {

    private GroupCrudRepository groupCrudRepository;
    private GroupMemberFinder memberFinder;

    public GroupEntity findById(Long id) throws GroupNotFoundException {
        if (id != null) {
            return groupCrudRepository.findById(id)
                    .orElseThrow(() -> new GroupNotFoundException(GroupCrudStatusEnum.GROUP_NOT_FOUND.toString()));
        } else
            throw new GroupNotFoundException(GroupCrudStatusEnum.ID_IS_NOT_SPECIFIED.toString() + ": " + id);
    }

    public GroupEntity findEntityByGroup(GroupDto group) throws GroupNotFoundException {
        Long id = group.getId();

        if (id != null)
            return findById(id);
        else
            return findEntityByGroupName(group.getGroupName());
    }


    public GroupEntity findEntityByGroupName(String groupName) throws GroupNotFoundException {
        Optional<GroupEntity> group = groupCrudRepository.findByGroupName(groupName);

        if (group.isPresent())
            return group.get();
        else
            throw new GroupNotFoundException(GroupCrudStatusEnum.GROUP_NOT_FOUND.toString());
    }

    public boolean isExist(GroupDto dto) {
        try {
            return findEntityByGroup(dto) != null;
        } catch (GroupNotFoundException e) {
            return false;
        }
    }

    public List<GroupEntity> findAllEntities() {
        return groupCrudRepository.findAll();
    }
}
