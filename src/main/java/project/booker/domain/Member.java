package project.booker.domain;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.booker.domain.Enum.Social;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberPk;

    @Column(unique = true)
    private String memberId;

    private String id;
    private String pw;
    private String name;
    private String email;
    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    private Social social;

    @Column(name="refreshToken")
    private String refreshToken;
    private LocalDate redate;

    @OneToOne(mappedBy = "member")
    private MemberProfile memberProfile;

    //----------------------------------------생성 메서드-------------------------------------------------------
    public static Member createMember(String id, String pw, String name, String email,
                                      LocalDate birth, Social social, LocalDate redate){
        Member member = new Member();
        member.memberId = NanoIdUtils.randomNanoId();
        member.id = id;
        member.pw = pw;
        member.name = name;
        member.email = email;
        member.birth = birth;
        member.social = social;
        member.redate = redate;

        return member;
    }

    //----------------------------------------수정 메서드-------------------------------------------------------
    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    //--------------------------------------연관 관계 메서드-----------------------------------------------------
    public void addMemberProfile(MemberProfile memberProfile){
        this.memberProfile = memberProfile;
    }

}
