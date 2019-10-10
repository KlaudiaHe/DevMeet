package pl.com.devmeet.devmeet.group_associated.permission.domain;

import pl.com.devmeet.devmeet.domain_utils.CrudEntityFinder;
import pl.com.devmeet.devmeet.domain_utils.CrudErrorEnum;
import pl.com.devmeet.devmeet.group_associated.group.domain.GroupCrudFacade;
import pl.com.devmeet.devmeet.group_associated.group.domain.GroupCrudRepository;
import pl.com.devmeet.devmeet.group_associated.group.domain.GroupDto;
import pl.com.devmeet.devmeet.group_associated.group.domain.GroupEntity;
import pl.com.devmeet.devmeet.group_associated.permission.domain.status.PermissionCrudInfoStatusEnum;
import pl.com.devmeet.devmeet.member_associated.member.domain.MemberCrudFacade;
import pl.com.devmeet.devmeet.member_associated.member.domain.MemberDto;
import pl.com.devmeet.devmeet.member_associated.member.domain.MemberEntity;

import java.util.List;
import java.util.Optional;

class PermissionCrudFinder implements CrudEntityFinder<PermissionDto, PermissionEntity> {

    private PermissionCrudRepository permissionRepository;
    private GroupCrudRepository groupRepository;
    private MemberCrudRepository memberRepository;

    public PermissionCrudFinder(PermissionCrudRepository permissionRepository, GroupCrudRepository groupRepository, MemberCrudRepository memberRepository) {
        this.permissionRepository = permissionRepository;
        this.groupRepository = groupRepository;
        this.memberRepository = memberRepository;
    }

    private MemberEntity findMemberEntity(MemberDto member) {
        return new MemberCrudFacade(memberRepository).findEntity(member);
    }

    private GroupEntity findGroupEntity(GroupDto group) {
        return new GroupCrudFacade(groupRepository).findEntity(group);
    }

    @Override
    public PermissionEntity findEntity(PermissionDto dto) throws IllegalArgumentException {
        Optional<PermissionEntity> permission = findPermission(dto);

        if(permission.isPresent())
            return permission.get();
        else
            throw new IllegalArgumentException(PermissionCrudInfoStatusEnum.PERMISSION_NOT_FOUND.toString());
    }

    private Optional<PermissionEntity> findPermission(PermissionDto dto) {
        MemberEntity member = findMemberEntity(dto.getMember());
        GroupEntity group = findGroupEntity(dto.getGroup());

        return permissionRepository.findByMemberAndGroup(member, group);
    }

    @Override
    public List<PermissionEntity> findEntities(PermissionDto dto) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(CrudErrorEnum.METHOD_NOT_IMPLEMENTED.toString());
    }

    @Override
    public boolean isExist(PermissionDto dto) {
        return findPermission(dto).isPresent();
    }
}
