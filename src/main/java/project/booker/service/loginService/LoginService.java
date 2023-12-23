package project.booker.service.loginService;

import project.booker.controller.LoginController.dto.request.JoinDto;
import project.booker.controller.LoginController.dto.request.LoginDto;
import project.booker.controller.LoginController.dto.response.AccessTokenDto;
import project.booker.domain.Member;
import project.booker.util.jwt.Jwt;

public interface LoginService {

    public void NomarlJoin(JoinDto joinDto);

    public Member VerifyUser(LoginDto loginDto);

    public void UpdateRefreshToken(String id, String refreshToken);

    public AccessTokenDto refreshToken(String refreshToken);

}
