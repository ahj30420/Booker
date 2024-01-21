package project.booker.controller.MessageController.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.booker.dto.ImgFileDto;

@Data
@NoArgsConstructor
public class SentMessageContent {

    private String title;
    private String content;
    private String nickname;
    private ImgFileDto imgFileDto;

    @Builder
    public SentMessageContent(String title, String content, String nickname, ImgFileDto imgFileDto) {
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.imgFileDto = imgFileDto;
    }

}
