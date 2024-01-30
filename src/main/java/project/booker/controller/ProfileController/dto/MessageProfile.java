package project.booker.controller.ProfileController.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.booker.dto.ImgFileDto;

@Data
@NoArgsConstructor
public class MessageProfile {

    private String nickname;
    private String imgURL;

    @Builder
    public MessageProfile(String nickname, String imgURL) {
        this.nickname = nickname;
        this.imgURL = imgURL;
    }
}
