package project.booker.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import project.booker.domain.Member;
import project.booker.domain.social.Social;

public interface LoginRepository extends JpaRepository<Member, Long> {

    public Member findDuplicatedIDByIdAndSocial(String id, Social social);

    @EntityGraph(attributePaths = {"memberProfile"})
    public Member findLoginByIdAndSocial(String id, Social social);

    @EntityGraph(attributePaths = {"memberProfile"})
    public Member findByRefreshToken(String refreshToken);

    public Member findMemberIdById(String id);
}
