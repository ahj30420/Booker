package project.booker.service.ProfileService;

import project.booker.controller.ProfileController.dto.SaveProfileDto;
import project.booker.domain.MemberProfile;
import project.booker.domain.embedded.UploadImg;

import java.util.Map;

public interface ProfileService {

    Map<String, Object> save(String memberId, SaveProfileDto saveProfileDto, UploadImg uploadImg);

    MemberProfile viewProfile(String profileId);
}
