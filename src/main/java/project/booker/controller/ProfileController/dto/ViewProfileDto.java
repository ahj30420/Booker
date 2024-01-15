package project.booker.controller.ProfileController.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewProfileDto {

    private String nickname;
    private String intro;
    private String base64Image;
    private String mimeType;
    private List<String> interets;

}
