package project.booker.controller.ProfileController.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.booker.dto.ImgFileDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewProfileDto {

    private String nickname;
    private String intro;
    private ImgFileDto imgFile;
    private List<String> interets;

}
