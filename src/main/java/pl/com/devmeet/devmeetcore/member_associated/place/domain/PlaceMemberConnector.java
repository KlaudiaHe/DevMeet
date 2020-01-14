package pl.com.devmeet.devmeetcore.member_associated.place.domain;

import pl.com.devmeet.devmeetcore.domain_utils.ObjectsConnector;
import pl.com.devmeet.devmeetcore.member_associated.member.domain.MemberEntity;

class PlaceMemberConnector implements ObjectsConnector<PlaceEntity, MemberEntity> {

    @Override
    public PlaceEntity connect(PlaceEntity output, MemberEntity input) {
        output.setMember(input);

        return output;
    }
}
