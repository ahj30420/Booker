package project.booker.service.OauthService;

import project.booker.controller.OauthController.dto.GoogleTokens;
import project.booker.controller.OauthController.dto.GoogleUserInfo;

import java.util.Map;

public interface GoogleOauthService {

    public GoogleTokens getGoogleToken(String code);

    GoogleUserInfo getGoogleInfo(GoogleTokens googleTokens);

    Map<String, Object> GoogleJoin(GoogleUserInfo googleUserInfo);
}
