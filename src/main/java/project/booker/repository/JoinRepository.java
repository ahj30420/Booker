package project.booker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.booker.domain.Member;
import project.booker.dto.joinController.request.JoinRequestDto;

public interface JoinRepository extends JpaRepository<Member, Long> {
    
    public Member findJoinByIdAndSocial(String id, String social);

}
