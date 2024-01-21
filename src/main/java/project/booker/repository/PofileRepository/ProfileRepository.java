package project.booker.repository.PofileRepository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import project.booker.domain.Follow;
import project.booker.domain.MemberProfile;

public interface ProfileRepository extends JpaRepository<MemberProfile, Long>, ProfileRepositoryCustom {

    public MemberProfile findByProfileId(String profileId);

    @EntityGraph(attributePaths = "interests")
    public MemberProfile findFetchInterestByProfileId(String profileId);

    public MemberProfile findProfileByNickname(String Nickname);
}
