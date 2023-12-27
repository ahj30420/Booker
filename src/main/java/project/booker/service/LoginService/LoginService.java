package project.booker.service.LoginService;

import project.booker.controller.LoginController.dto.request.JoinDto;
import project.booker.controller.LoginController.dto.request.LoginDto;
import project.booker.controller.LoginController.dto.response.AccessTokenDto;
import project.booker.domain.Member;

public interface LoginService {

    Long NomarlJoin(JoinDto joinDto);

    Member VerifyUser(LoginDto loginDto);

    void UpdateRefreshToken(Long id, String refreshToken);

    AccessTokenDto refreshToken(String refreshToken);

}
