package pl.com.devmeet.devmeetcore.test_utils;

import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberCrudService;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberDto;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberRepository;

/**
 * Created by IntelliJ IDEA.
 * User: Kamil Ptasinski
 * Date: 30.11.2019
 * Time: 13:14
 */
class TestFirstMemberInitiator implements TestObjectInitiator<MemberRepository, MemberCrudService, MemberDto> {

    private MemberRepository repository;
    private MemberDto testMemberDto;

    public TestFirstMemberInitiator(MemberRepository repository) {
        this.repository = repository;


    }

    @Override
    public MemberCrudService initFacade() {
        return null;
    }

    @Override
    public MemberDto initAndSaveTestObject() {
        return null;
    }
}
