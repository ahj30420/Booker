package project.booker.controller.OauthController.dto;

import lombok.Data;

@Data
public class NaverTokens {

    private String access_token;
    private String refresh_token;
    private String token_type;
    private String expires_in;

}
