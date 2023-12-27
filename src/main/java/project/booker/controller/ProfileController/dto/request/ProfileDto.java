package project.booker.controller.ProfileController.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileDto {

    @Pattern(regexp = "^[가-힣a-zA-Z0-9_]{1,15}$")
    private String nickname;

    @Size(max=30)
    private String intro;
    private MultipartFile imageFile;
    private String interest1;
    private String interest2;
    private String interest3;
    private String interest4;
    private String interest5;

}
