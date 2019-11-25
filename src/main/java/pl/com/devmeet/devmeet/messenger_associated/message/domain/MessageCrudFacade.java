package pl.com.devmeet.devmeet.messenger_associated.message.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.devmeet.devmeet.domain_utils.EntityNotFoundException;

import java.util.List;

@Service
public class MessageCrudFacade {

    private MessageRepository repository;

    @Autowired
    public MessageCrudFacade(MessageRepository repository) {
        this.repository = repository;
    }

    private MessageCrudFinder finderInit() {
        return new MessageCrudFinder(repository);
    }

    private MessageCrudCreator creatorInit() {
        return new MessageCrudCreator(repository);
    }

    private MessageCrudUpdater updaterInit() {
        return new MessageCrudUpdater(repository);
    }

    private MessageCrudDeleter deleterInit() {
        return new MessageCrudDeleter(repository);
    }

    public MessageDto create(MessageDto dto) {
        return creatorInit().create(dto);
    }

    public List<MessageEntity> findEntityFromMember(String memberNick) {
        return finderInit().findEntityByFromMember(memberNick);
    }

    public List<MessageEntity> findEntityToMember(String memberNick) {
        return finderInit().findEntityByToMember(memberNick);
    }

    public MessageDto update(MessageDto newDto, MessageDto oldDto) {

        return map(updaterInit().updateEntity(oldDto, newDto));
    }

    public boolean deleteMessagesSentToMember(MessageDto dto) throws EntityNotFoundException { //usuń wiadomości wysłane do konkretnego Membera
        return deleterInit().delete(dto.getToMember().getNick());
    }

    public boolean deleteMessagesSentFromMember(MessageDto dto) throws EntityNotFoundException { //usuń wiadomości wysłane do konkretnego Membera
        return deleterInit().delete(dto.getFromMember().getNick());
    }

    public static MessageDto map(MessageEntity entity) {
        return MessageMapper.toDto(entity);
    }

    public static MessageEntity map(MessageDto dto) {
        return MessageMapper.toEntity(dto);
    }
}