package project.booker.controller.MessageController.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.booker.dto.ImgFileDto;

@Data
@NoArgsConstructor
public class ReceivedMessageContent {

    private String senderId;
    private String title;
    private String content;
    private String nickname;
    private ImgFileDto imgFileDto;

    @Builder
    public ReceivedMessageContent(String senderId, String title, String content, String nickname, ImgFileDto imgFileDto) {
        this.senderId = senderId;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.imgFileDto = imgFileDto;
    }
}
