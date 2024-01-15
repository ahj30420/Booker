package project.booker.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import project.booker.domain.Follow;
import project.booker.domain.MemberProfile;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    public Long countByFollower(MemberProfile follower);

    public Long countByFollowing(MemberProfile following);

    @EntityGraph(attributePaths = {"follower"})
    public List<Follow> findAllByFollowing(MemberProfile memberProfile);

    @EntityGraph(attributePaths = {"following"})
    public List<Follow> findAllByFollower(MemberProfile memberProfile);

    void deleteByFollowerAndFollowing(MemberProfile userProfile, MemberProfile targetProfile);

    Follow findByFollowerAndFollowing(MemberProfile userProfile, MemberProfile targetProfile);
}
