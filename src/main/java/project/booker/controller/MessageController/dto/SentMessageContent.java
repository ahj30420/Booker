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
    private String imgURL;

    @Builder
    public SentMessageContent(String title, String content, String nickname, String imgURL) {
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.imgURL = imgURL;
    }

}
