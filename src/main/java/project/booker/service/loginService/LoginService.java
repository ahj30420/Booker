package project.booker.service.loginService;

import project.booker.controller.LoginController.dto.request.JoinRequestDto;
import project.booker.controller.LoginController.dto.request.LoginRequestDto;
import project.booker.domain.Member;
import project.booker.util.jwt.Jwt;

public interface LoginService {

    public void NomarlJoin(JoinRequestDto joinRequestDto);

    public Member VerifyUser(LoginRequestDto loginRequestDto);

    public void UpdateRefreshToken(String id, String refreshToken);

    public Jwt refreshToken(String refreshToken);

}
