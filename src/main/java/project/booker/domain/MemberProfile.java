package project.booker.domain;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.booker.domain.Enum.Sharing;
import project.booker.domain.embedded.UploadImg;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profilePk;

    @Column(unique = true)
    private String profileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_pk")
    private Member member;

    private String nickname;
    private String intro;

    @Embedded
    private UploadImg img;

    @OneToMany(mappedBy = "memberProfile")
    private List<Interest> interests = new ArrayList<>();

    @OneToMany(mappedBy = "memberProfile")
    private List<Book> books = new ArrayList<>();

    //----------------------------------------생성 메서드-------------------------------------------------------
    public static MemberProfile createMemberProfile(Member member, String nickname, String intro, UploadImg img){
        MemberProfile memberProfile = new MemberProfile();
        memberProfile.profileId = NanoIdUtils.randomNanoId();
        memberProfile.addMember(member);
        memberProfile.nickname = nickname;
        memberProfile.intro = intro;
        memberProfile.img = img;

        return memberProfile;
    }

    //----------------------------------------수정 메서드-------------------------------------------------------
    public void updateProfile(String intro, UploadImg uploadImg) {
        if(intro != null){
            this.intro = intro;
        }
        if(uploadImg != null){
            this.img = uploadImg;
        }
    }

    //--------------------------------------연관 관계 메서드-----------------------------------------------------
    public void addMember(Member member){
        this.member = member;
        member.addMemberProfile(this);
    }

}
