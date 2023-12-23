package project.booker.controller.LoginController.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessTokenDto {
    private String accessToken;
}
