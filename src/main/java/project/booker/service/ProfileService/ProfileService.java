package project.booker.service.ProfileService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import project.booker.controller.ProfileController.dto.SaveProfileDto;
import project.booker.domain.MemberProfile;
import project.booker.domain.embedded.UploadImg;

import java.util.Map;

public interface ProfileService {

    Map<String, Object> save(String memberId, SaveProfileDto saveProfileDto, UploadImg uploadImg);

    MemberProfile viewProfile(String profileId);

    Slice<MemberProfile> recommendUser(String profileId, Pageable pageable);
}
