package pl.com.devmeet.devmeetcore.user.domain;

class UserMapper {

    static UserDto toDto(UserEntity entity) {
        return new UserDto().builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .creationTime(entity.getCreationTime())
                .modificationTime(entity.getModificationTime())
                .isActive(entity.isActive())
                .activationKey(entity.getActivationKey())
                .lastLoggedIn(entity.getLastLoggedIn())
                .build();
    }

    static UserEntity toEntity(UserDto dto) {
        return new UserEntity().builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .creationTime(dto.getCreationTime())
                .modificationTime(dto.getModificationTime())
                .isActive(dto.isActive())
                .activationKey(dto.getActivationKey())
                .lastLoggedIn(dto.getLastLoggedIn())
                .build();
    }
}
