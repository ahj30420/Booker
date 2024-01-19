package project.booker.controller.ProfileController.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.booker.dto.ImgFileDto;

import java.util.List;

@Data
@NoArgsConstructor
public class RecommendProfileDto {

    private String profileId;
    private String nickname;
    private ImgFileDto imgFileDto;
    private List<String> interests;

    @Builder
    public RecommendProfileDto(String profileId, String nickname, ImgFileDto imgFileDto, List<String> interests) {
        this.profileId = profileId;
        this.nickname = nickname;
        this.imgFileDto = imgFileDto;
        this.interests = interests;
    }
}
