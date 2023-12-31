package project.booker.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.booker.domain.embedded.Interest;
import project.booker.domain.embedded.UploadImg;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileIdx;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private Member member;

    @OneToMany(mappedBy = "memberProfile")
    private List<Book> books = new ArrayList<>();

    private String nickname;
    private String intro;

    @Embedded
    private UploadImg img;

    @Embedded
    private Interest interest;

    //----------------------------------------생성 메서드-------------------------------------------------------
    public static MemberProfile createMemberProfile(Member member, String nickname, String intro, UploadImg img, Interest interest){
        MemberProfile memberProfile = new MemberProfile();
        memberProfile.addMember(member);
        memberProfile.nickname = nickname;
        memberProfile.intro = intro;
        memberProfile.img = img;
        memberProfile.interest = interest;

        return memberProfile;
    }

    //--------------------------------------연관 관계 메서드-----------------------------------------------------
    public void addMember(Member member){
        this.member = member;
        member.addMemberProfile(this);
    }

    //----------------------------------------비즈니스 로직------------------------------------------------------

}
