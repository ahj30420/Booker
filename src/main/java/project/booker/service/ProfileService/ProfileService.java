package project.booker.service.ProfileService;

import project.booker.controller.ProfileController.dto.ProfileDto;
import project.booker.controller.ProfileController.dto.UploadImgDto;
import project.booker.util.jwt.Jwt;

import java.util.Map;

public interface ProfileService {

    Map<String, Object> save(Long memberIdx, ProfileDto profileDto, UploadImgDto uploadImgDto);

    String getStoreImgName(Long MemberIdx);

}
