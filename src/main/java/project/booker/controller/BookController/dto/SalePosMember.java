package project.booker.controller.BookController.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.booker.dto.ImgFileDto;

@Data
@NoArgsConstructor
public class SalePosMember {

    private String profileId;
    private String imgURL;
    private String nickname;

    @Builder
    public SalePosMember(String profileId, String imgURL, String nickname) {
        this.profileId = profileId;
        this.imgURL = imgURL;
        this.nickname = nickname;
    }
}
