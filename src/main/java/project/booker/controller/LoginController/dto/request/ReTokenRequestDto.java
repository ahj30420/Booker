package project.booker.controller.LoginController.dto.request;

import lombok.Data;

@Data
public class ReTokenRequestDto {
    private String refreshToken;
}
