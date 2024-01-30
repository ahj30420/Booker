package project.booker.controller.FollowController.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.booker.dto.ImgFileDto;

@Data
@NoArgsConstructor
public class FollowProfile {

    private String profileId;
    private String nickname;
    private String intro;
    private String imgURL;

    @Builder
    public FollowProfile(String profileId, String nickname, String intro, String imgURL) {
        this.profileId = profileId;
        this.nickname = nickname;
        this.intro = intro;
        this.imgURL = imgURL;
    }

}
