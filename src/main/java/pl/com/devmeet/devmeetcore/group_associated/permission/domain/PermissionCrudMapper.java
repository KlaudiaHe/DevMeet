package pl.com.devmeet.devmeetcore.group_associated.permission.domain;

class PermissionCrudMapper {

    public static PermissionDto map (PermissionEntity entity){
        return entity != null ? new PermissionDto().builder()
//                .member(MemberCrudService.map(entity.getMember()))
//                .group(GroupCrudService.map(entity.getGroup()))
                .possibleToVote(entity.isPossibleToVote())
                .possibleToMessaging(entity.isPossibleToMessaging())
                .possibleToChangeGroupName(entity.isPossibleToChangeGroupName())
                .possibleToBanMember(entity.isPossibleToBanMember())
                .memberBaned(entity.isMemberBaned())
                .creationTime(entity.getCreationTime())
                .modificationTime(entity.getModificationTime())
                .isActive(entity.isActive())
                .build() : null;
    }

    public static PermissionEntity map (PermissionDto dto){
        return dto != null ? new PermissionEntity().builder()
//                .member(MemberCrudService.map(dto.getMember()))
//                .group(GroupCrudService.map(dto.getGroup()))
                .possibleToVote(dto.isPossibleToVote())
                .possibleToMessaging(dto.isPossibleToMessaging())
                .possibleToChangeGroupName(dto.isPossibleToChangeGroupName())
                .possibleToBanMember(dto.isPossibleToBanMember())
                .memberBaned(dto.isMemberBaned())
                .creationTime(dto.getCreationTime())
                .modificationTime(dto.getModificationTime())
                .isActive(dto.isActive())
                .build() : null;
    }
}
