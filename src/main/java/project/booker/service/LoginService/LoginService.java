package project.booker.service.LoginService;

import project.booker.controller.LoginController.dto.JoinDto;
import project.booker.controller.LoginController.dto.LoginDto;
import project.booker.controller.LoginController.dto.AccessTokenDto;
import project.booker.domain.Member;

public interface LoginService {

    String NomarlJoin(JoinDto joinDto);

    Member VerifyUser(LoginDto loginDto);

    void UpdateRefreshToken(Long id, String refreshToken);

    AccessTokenDto refreshToken(String refreshToken);

}
