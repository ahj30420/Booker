package project.booker.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_pk")
    private MemberProfile memberProfile;

    private String interest;

    //----------------------------------------생성 메서드-------------------------------------------------------
    public static Interest createInterest(MemberProfile memberProfile, String interestInfo){
        Interest interest = new Interest();
        interest.memberProfile = memberProfile;
        interest.interest = interestInfo;

        return interest;
    }

    //--------------------------------------연관 관계 메서드-----------------------------------------------------
    public void addMemberProfile(MemberProfile memberProfile){
        this.memberProfile = memberProfile;
        memberProfile.getInterests().add(this);
    }

}
