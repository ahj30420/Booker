package project.booker.controller.LoginController.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginRequestDto {

    private String id;
    private String pw;

}
