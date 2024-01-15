package project.booker.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import project.booker.domain.Follow;
import project.booker.domain.MemberProfile;

public interface ProfileRepository extends JpaRepository<MemberProfile, Long> {

    public MemberProfile findByProfileId(String profileId);

    public MemberProfile findProfileByNickname(String Nickname);

}
