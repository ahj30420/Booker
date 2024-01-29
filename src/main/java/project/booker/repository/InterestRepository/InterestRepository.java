package project.booker.repository.InterestRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.booker.domain.Interest;
import project.booker.domain.MemberProfile;

public interface InterestRepository extends JpaRepository<Interest, Long>, InterestRepositoryCustom {

    @Modifying
    @Query("delete from Interest i where i.memberProfile = :profile")
    void deleteByMemberProfile(@Param("profile") MemberProfile profile);

}
