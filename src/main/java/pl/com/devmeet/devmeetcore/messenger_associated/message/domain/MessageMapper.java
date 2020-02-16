package pl.com.devmeet.devmeetcore.messenger_associated.message.domain;

import pl.com.devmeet.devmeetcore.messenger_associated.messenger.domain.MessengerCrudService;

class MessageMapper {

    static MessageDto toDto(MessageEntity messageEntity) {

        return messageEntity != null ? MessageDto.builder()
                .id(messageEntity.getId())
                .sender(MessengerCrudService.map(messageEntity.getSender()))
                .receiver(MessengerCrudService.map(messageEntity.getReceiver()))
                .message(messageEntity.getMessage())
                .modificationTime(messageEntity.getModificationTime())
                .creationTime(messageEntity.getCreationTime())
                .isActive(messageEntity.isActive())
                .build() : null;
    }

    static MessageEntity toEntity(MessageDto messageDto) {
        return messageDto != null ? MessageEntity.builder()
                .id(messageDto.getId())
                .sender(MessengerCrudService.map(messageDto.getSender()))
                .receiver(MessengerCrudService.map(messageDto.getReceiver()))
                .message(messageDto.getMessage())
                .modificationTime(messageDto.getModificationTime())
                .creationTime(messageDto.getCreationTime())
                .isActive(messageDto.isActive())
                .build() : null;
    }
}
