package project.booker.service.ProfileService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import project.booker.controller.ProfileController.dto.*;
import project.booker.domain.MemberProfile;
import project.booker.domain.embedded.UploadImg;

import java.io.IOException;
import java.util.Map;

public interface ProfileService {

    Map<String, Object> save(String memberId, SaveProfileDto saveProfileDto, UploadImg uploadImg);

    ViewProfileDto viewProfile(String profileId) throws IOException;

    RecommendList recommendUser(String profileId, Pageable pageable) throws IOException;

    SearchProfileList searchProfile(String nickname) throws IOException;

    MessageProfile searchSimpleProfile(String profileId) throws IOException;
}
