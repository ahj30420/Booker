package project.booker.controller.MessageController.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.booker.domain.Enum.State;
import project.booker.dto.ImgFileDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MessageInfo {

    private String messageId;
    private String title;
    private State state;
    private LocalDateTime redate;
    private String nickname;
    private String imgURL;

    @Builder
    public MessageInfo(String messageId, String title, State state, LocalDateTime redate, String nickname, String imgURL) {
        this.messageId = messageId;
        this.title = title;
        this.state = state;
        this.redate = redate;
        this.nickname = nickname;
        this.imgURL = imgURL;
    }

}
