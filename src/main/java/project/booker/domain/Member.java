package project.booker.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_idx;

    private String id;
    private String pw;
    private String name;
    private String email;
    private LocalDate birth;
    private String social;
    private String refreshToken;
    private LocalDate redate;

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

    //----------------------------------------비즈니스 로직------------------------------------------------------

}
