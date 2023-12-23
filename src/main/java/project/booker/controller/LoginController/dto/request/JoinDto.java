package project.booker.controller.LoginController.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class JoinDto {

    @NotBlank
    private String id;

    /**
     * <조건>
     * 1. 최소 하나 이상의 알파벳, 숫자, 특수문자가 들어가야된다.
     * 2. 7~15글자 여야 한다.
     */
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{7,15}$")
    private String pw;

    /**
     * <조건>
     * 1. 한글 혹은 알파벳으로만 구성되어야 한다.
     * 2. 1~6글자 여야 한다.
     */
    @NotBlank
    @Pattern(regexp = "^[가-힣a-zA-Z]{1,6}$")
    private String name;

    @Email
    @NotBlank
    private String email;

    private LocalDate birth;

}
