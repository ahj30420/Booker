package project.booker.service.FollowService;

import project.booker.controller.FollowController.dto.FollowCount;
import project.booker.controller.FollowController.dto.FollowProfile;
import project.booker.controller.FollowController.dto.IsFollowing;
import project.booker.domain.Follow;
import project.booker.domain.MemberProfile;

import java.io.IOException;
import java.util.List;

public interface FollowService {
    FollowCount searchFollowCount(String profileId);

    List<FollowProfile> searchFollowers(String profileId) throws IOException;

    List<FollowProfile> searchFollowing(String profileId) throws IOException;

    void Subscribe(String userProfileId, String targetProfileId);

    void CancelSubscribe(String userProfileId, String targetProfileId);

    IsFollowing hasFollowed(String userProfileId, String targetProfileId);
}
