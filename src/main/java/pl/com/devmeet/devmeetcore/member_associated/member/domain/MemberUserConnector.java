package pl.com.devmeet.devmeetcore.member_associated.member.domain;

import pl.com.devmeet.devmeetcore.domain_utils.ObjectsConnector;
import pl.com.devmeet.devmeetcore.user.domain.UserEntity;

class MemberUserConnector implements ObjectsConnector<MemberEntity, UserEntity> {
    @Override
    public MemberEntity connect(MemberEntity output, UserEntity input) {
        output.setUser(input);

        return output;
    }
}
