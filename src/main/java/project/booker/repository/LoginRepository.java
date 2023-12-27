package project.booker.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import project.booker.domain.Member;

public interface LoginRepository extends JpaRepository<Member, Long> {

    public Member findDuplicatedIDByIdAndSocial(String id, String social);

    @EntityGraph(attributePaths = {"memberProfile"})
    public Member findLoginByIdAndSocial(String id, String social);

    @EntityGraph(attributePaths = {"memberProfile"})
    public Member findByRefreshToken(String refreshToken);

}
