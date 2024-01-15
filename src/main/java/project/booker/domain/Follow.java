package project.booker.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower")
    private MemberProfile follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following")
    private MemberProfile following;

    //----------------------------------------생성 메서드-------------------------------------------------------
    public static Follow createFollow(MemberProfile follower, MemberProfile following) {
        Follow follow = new Follow();
        follow.follower = follower;
        follow.following = following;

        return follow;
    }

}
