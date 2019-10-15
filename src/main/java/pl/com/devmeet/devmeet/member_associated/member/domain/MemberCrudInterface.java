package pl.com.devmeet.devmeet.member_associated.member.domain;

import pl.com.devmeet.devmeet.domain_utils.EntityAlreadyExistsException;
import pl.com.devmeet.devmeet.domain_utils.EntityNotFoundException;

public interface MemberCrudInterface {

    MemberDto create(MemberDto memberDto) throws MemberNotFoundException, MemberAlreadyExistsException, EntityNotFoundException, EntityAlreadyExistsException;

    MemberEntity findEntity(MemberDto memberDto) throws MemberNotFoundException, EntityNotFoundException;

    MemberDto read(MemberDto memberDto) throws MemberNotFoundException, EntityNotFoundException;

    MemberDto update(MemberDto oldDto, MemberDto newDto) throws MemberNotFoundException, EntityNotFoundException;

    MemberEntity delete(MemberDto memberDto) throws EntityNotFoundException;

    boolean isActive(MemberDto memberDto) throws MemberNotFoundException, EntityNotFoundException;

    boolean doesExist(MemberDto memberDto) throws MemberNotFoundException, EntityNotFoundException;

   // Optional<List<MemberEntity>> memberEntityList(GroupDto groupDto);

   // Optional<List<MemberEntity>> memberEntityList(PlaceDto placeDto);

    static MemberDto map(MemberEntity memberEntity) {
        return MemberMapper.map(memberEntity);
    }

    static MemberEntity map(MemberDto memberDto) {
        return MemberMapper.map(memberDto);
    }
}