package project.booker.service.OauthService;

import project.booker.controller.OauthController.dto.NaverTokens;
import project.booker.controller.OauthController.dto.NaverUserInfo;

import java.util.Map;

public interface NaverOauthService {

    NaverTokens getNaverToken(String code, String state);

    NaverUserInfo getNaverInfo(NaverTokens naverTokens);

    Map<String, Object> NaverJoin(NaverUserInfo naverUserInfo);

}
