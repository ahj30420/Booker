package project.booker.service.ProfileService;

import project.booker.controller.ProfileController.dto.request.ProfileDto;
import project.booker.controller.ProfileController.dto.request.UploadImgDto;

public interface ProfileService {
    void save(Long memberIdx, ProfileDto profileDto, UploadImgDto uploadImgDto);
    String getStoreImgName(Long MemberIdx);
}
