package project.booker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.booker.controller.LoginController.dto.request.LoginRequestDto;
import project.booker.domain.Member;

public interface LoginRepository extends JpaRepository<Member, Long> {

    public Member findLoginByIdAndSocial(String id, String social);

    public Member findByRefreshToken(String refreshToken);

}
