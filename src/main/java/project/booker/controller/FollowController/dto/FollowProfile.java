package project.booker.controller.FollowController.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FollowProfile {

    private String nickname;
    private String intro;
    private String base64Image;
    private String mimeType;

    @Builder
    public FollowProfile(String nickname, String intro, String base64Image, String mimeType) {
        this.nickname = nickname;
        this.intro = intro;
        this.base64Image = base64Image;
        this.mimeType = mimeType;
    }

}
