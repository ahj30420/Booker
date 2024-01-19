package project.booker.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImgFileDto {

    private String base64Image;
    private String mimeType;

    @Builder
    public ImgFileDto(String base64Image, String mimeType) {
        this.base64Image = base64Image;
        this.mimeType = mimeType;
    }

}
