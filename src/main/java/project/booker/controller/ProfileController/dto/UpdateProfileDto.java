package project.booker.controller.ProfileController.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileDto {

    @Size(max=30)
    private String intro;

    private boolean defaultImg;

    private MultipartFile imageFile;

    private List<String> interestList = new ArrayList<>();

}
