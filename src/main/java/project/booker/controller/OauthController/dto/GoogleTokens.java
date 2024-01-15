package project.booker.controller.OauthController.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class GoogleTokens {

    private String access_token;
    private int expires_in;
    private String scope;
    private String token_type;
    private String id_token;

}
