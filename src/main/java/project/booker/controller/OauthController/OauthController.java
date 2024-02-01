package project.booker.controller.OauthController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.booker.controller.OauthController.dto.*;
import project.booker.domain.Member;
import project.booker.dto.*;
import project.booker.service.LoginService.LoginService;
import project.booker.service.OauthService.GoogleOauthService;
import project.booker.service.OauthService.NaverOauthService;
import project.booker.util.jwt.Jwt;
import project.booker.util.jwt.JwtProvider;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/oauth2/code")
@RequiredArgsConstructor
public class OauthController {

    private final NaverOauthService naverOauthService;
    private final GoogleOauthService googleOauthService;
    private final LoginService loginService;
    private final JwtProvider jwtProvider;

    /**
     * 네이버 소셜 로그인
     * 1. Client에게 Code, State 받기
     * 2. Code로 네이버의 AccessToken 발급
     * 3. AccessToken으로 회원 정보 조회
     * 4. 조회한 정보로 회원가입(이미 회원일 경우 가입 X)
     * 5-1. 회원가입 후 프로필 등록을 위해 회원 idx 반환(신규 회원으로 회원가입 했을 경우)
     * 5-2. 기존 회원일 경우 프로필 등록 과정이 필요하지 않으므로 JWT를 바로 발급해준다.(기존 회원일 경우)
     */
    @PostMapping("/naver")
    public Map<String, String> Oauth2LoginNaver(@RequestBody NaverCode naverCode){

        Map<String, String> result = new HashMap<>();

        String code = naverCode.getCode();
        String state = naverCode.getState();

        NaverTokens naverTokens = naverOauthService.getNaverToken(code, state);
        NaverUserInfo naverUserInfo = naverOauthService.getNaverInfo(naverTokens);

        Map<String, Object> joinMap = naverOauthService.NaverJoin(naverUserInfo);
        Member member = (Member) joinMap.get("member");

        if((Boolean) joinMap.get("isNewMember")){
            result.put("memberId", member.getMemberId());
            return result;
        }

        Jwt jwt = createJwt(member);

        result.put("accessToken", jwt.getAccessToken());
        result.put("refreshToken", jwt.getRefreshToken());
        result.put("nickname", member.getMemberProfile().getNickname());

        return result;
    }


    /**
     * 구글 소셜 로그인
     * 1. Client에게 Code 받기
     * 2. Code로 구글의 AccessToken 발급
     * 3. AccessToken으로 회원 정보 조회
     * 4. 조회한 정보로 회원가입(이미 회원일 경우 가입 X)
     * 5-1. 회원가입 후 프로필 등록을 위해 회원 idx 반환(신규 회원으로 회원가입 했을 경우)
     * 5-2. 기존 회원일 경우 프로필 등록 과정이 필요하지 않으므로 JWT를 바로 발급해준다.(기존 회원일 경우)
     */
    @PostMapping("/google")
    public Map<String, Object> Oauth2LoginGoogle(@RequestBody GoogleCode googleCode){

        Map<String, Object> result = new HashMap<>();

        String code = googleCode.getCode();

        GoogleTokens googleTokens = googleOauthService.getGoogleToken(code);
        GoogleUserInfo googleUserInfo = googleOauthService.getGoogleInfo(googleTokens);

        Map<String, Object> joinMap = googleOauthService.GoogleJoin(googleUserInfo);
        Member member = (Member) joinMap.get("member");

        if((Boolean) joinMap.get("isNewMember")){
            result.put("memberId", member.getMemberId());
            return result;
        }

        Jwt jwt = createJwt(member);

        result.put("accessToken", jwt.getAccessToken());
        result.put("refreshToken", jwt.getRefreshToken());
        result.put("nickname", member.getMemberProfile().getNickname());

        return result;
    }

    //--------------------------------------Private Method-----------------------------------------------------

    /**
     * JWT 생성하기
     * 1. 회원 정보 중 idx, 이름, 닉네임 정보로 JWT를 발급한다.
     */
    private Jwt createJwt(Member member) {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(member.getMemberProfile().getProfileId(), member.getName(), member.getMemberProfile().getNickname());

        Map<String, Object> claims = new HashMap<>();
        claims.put("AuthenticetedUser", authenticatedUser);

        Jwt jwt = jwtProvider.createJwt(claims);
        loginService.UpdateRefreshToken(member.getMemberPk(), jwt.getRefreshToken());

        return jwt;
    }

}
