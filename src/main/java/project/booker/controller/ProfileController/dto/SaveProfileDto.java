package project.booker.controller.ProfileController.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class SaveProfileDto {

    /**
     * <조건>
     * 1. 한글, 영문, 숫자, 밑줄로 이루어진 문자열(1~15 글자 이내)
     */
    @Pattern(regexp = "^[가-힣a-zA-Z0-9_]{1,15}$")
    private String nickname;


    /**
     * <조건>
     * 1. 최대 30글자 이내
     */
    @Size(max=30)
    private String intro;

    private MultipartFile imageFile;
    private List<String> interestList = new ArrayList<>();

}