package project.booker.controller.LoginController.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginDto {

    private String id;
    private String pw;

}
