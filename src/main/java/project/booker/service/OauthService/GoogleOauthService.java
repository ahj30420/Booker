package project.booker.service.OauthService;

import project.booker.dto.GoogleTokens;
import project.booker.dto.GoogleUserInfo;

import java.util.Map;

public interface GoogleOauthService {

    public GoogleTokens getGoogleToken(String code);

    GoogleUserInfo getGoogleInfo(GoogleTokens googleTokens);

    Map<String, Object> GoogleJoin(GoogleUserInfo googleUserInfo);
}
