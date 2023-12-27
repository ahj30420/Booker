package project.booker.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MemberIdx;

    private String id;
    private String pw;
    private String name;
    private String email;
    private LocalDate birth;
    private String social;

    @Column(name="refreshToken")
    private String refreshToken;
    private LocalDate redate;

    @OneToOne(mappedBy = "member")
    private MemberProfile memberProfile;

    //----------------------------------------생성 메서드-------------------------------------------------------
    public static Member createMember(String id, String pw, String name, String email,
                                      LocalDate birth, String social, LocalDate redate){
        Member member = new Member();
        member.id = id;
        member.pw = pw;
        member.name = name;
        member.email = email;
        member.birth = birth;
        member.social = social;
        member.redate = redate;

        return member;
    }

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    //--------------------------------------연관 관계 메서드-----------------------------------------------------
    public void addMemberProfile(MemberProfile memberProfile){
        this.memberProfile = memberProfile;
    }

    //----------------------------------------비즈니스 로직------------------------------------------------------

}
