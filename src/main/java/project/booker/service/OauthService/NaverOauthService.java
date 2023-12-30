package project.booker.service.OauthService;

import project.booker.domain.Member;
import project.booker.dto.NaverTokens;
import project.booker.dto.NaverUserInfo;
import project.booker.util.jwt.Jwt;

import java.util.Map;

public interface NaverOauthService {

    NaverTokens getNaverToken(String code, String state);

    NaverUserInfo getNaverInfo(NaverTokens naverTokens);

    Map<String, Object> NaverJoin(NaverUserInfo naverUserInfo);

}
