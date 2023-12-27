package project.booker.controller.ProfileController.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadImgDto {

    private String RealImgName;
    private String StoreImgName;

}
