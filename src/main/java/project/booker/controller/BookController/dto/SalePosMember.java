package project.booker.controller.BookController.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.booker.dto.ImgFileDto;

@Data
@NoArgsConstructor
public class SalePosMember {

    private String profileId;
    private ImgFileDto imgFileDto;
    private String nickname;

    @Builder
    public SalePosMember(String profileId, ImgFileDto imgFileDto, String nickname) {
        this.profileId = profileId;
        this.imgFileDto = imgFileDto;
        this.nickname = nickname;
    }
}
