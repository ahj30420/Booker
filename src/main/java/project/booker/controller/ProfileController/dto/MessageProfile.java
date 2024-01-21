package project.booker.controller.ProfileController.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.booker.dto.ImgFileDto;

@Data
@NoArgsConstructor
public class MessageProfile {

    private String nickname;
    private ImgFileDto imgFileDto;

    @Builder
    public MessageProfile(String nickname, ImgFileDto imgFileDto) {
        this.nickname = nickname;
        this.imgFileDto = imgFileDto;
    }
}
