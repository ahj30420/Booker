package project.booker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.booker.domain.MemberProfile;

public interface ProfileRepository extends JpaRepository<MemberProfile, Long> {

    public MemberProfile findProfileByNickname(String Nickname);

}
