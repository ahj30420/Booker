package project.booker.service.ProfileService;

import project.booker.controller.ProfileController.dto.ProfileDto;
import project.booker.domain.embedded.UploadImg;

import java.util.Map;

public interface ProfileService {

    Map<String, Object> save(String memberId, ProfileDto profileDto, UploadImg uploadImg);

    String getStoreImgName(String prfileId);

}
