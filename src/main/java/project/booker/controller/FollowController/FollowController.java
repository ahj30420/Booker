package project.booker.controller.FollowController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.booker.controller.FollowController.dto.FollowCount;
import project.booker.controller.FollowController.dto.FollowProfile;
import project.booker.controller.FollowController.dto.IsFollowing;
import project.booker.controller.FollowController.dto.TargetProfile;
import project.booker.dto.AuthenticatedUser;
import project.booker.service.FollowService.FollowService;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    /**
     * 팔로우 수 조회
     *
     * @param profileId: 사용자의 팔로우 수 조회인지 다른 사람의 팔로우 수 조회인지 구분하기 위해 사용
     * 1. 팔로우 수 정보 조회의 대상이 되는 사용자 구분
     * 2. 팔로워 / 팔로잉 수 조회
     */
    @GetMapping("/follow/count")
    public FollowCount getFollowCount(HttpServletRequest request, @RequestParam(name = "profileId", required = false) String profileId){

        if(profileId == null){
            AuthenticatedUser user = (AuthenticatedUser) request.getAttribute("AuthenticatedUser");
            profileId = user.getProfileId();
        }

        FollowCount followCount = followService.searchFollowCount(profileId);

        return followCount;
    }

    /**
     * 팔로워 회원 목록 조회
     *
     *  @param profileId: 사용자의 팔로워 조회인지 다른 사람의 팔로워 조회인지 구분하기 위해 사용
     *  1. 어떤 사용자의 팔로워를 조회 할 것인지 결정
     *  2. 해당 사용자를 팔로우 하고 있는 팔로우 정보 조회
     */
    @GetMapping("/follower/list")
    public List<FollowProfile> getFollowerList(HttpServletRequest request, @RequestParam(name = "profileId", required = false) String profileId) throws IOException {

        if(profileId == null){
            AuthenticatedUser user = (AuthenticatedUser) request.getAttribute("AuthenticatedUser");
            profileId = user.getProfileId();
        }

        return followService.searchFollowers(profileId);
    }

    /**
     * 팔로잉 회원 목록 조회
     *
     *  @param profileId: 사용자의 팔로잉 조회인지 다른 사람의 팔로잉 조회인지 구분하기 위해 사용
     *  1. 어떤 사용자의 팔로잉를 조회 할 것인지 결정
     *  2. 해당 사용자가 팔로우 하고 있는 팔로우 정보 조회
     *  3. 팔로잉의 닉네임, 소개글, 이미지 전송을 위해 DTO로 변환
     */
    @GetMapping("/following/list")
    public List<FollowProfile> getFollowingList(HttpServletRequest request, @RequestParam(name = "profileId", required = false) String profileId) throws IOException {

        if(profileId == null){
            AuthenticatedUser user = (AuthenticatedUser) request.getAttribute("AuthenticatedUser");
            profileId = user.getProfileId();
        }

        return followService.searchFollowing(profileId);
    }


    /**
     * 팔로잉 하기
     */
    @PostMapping("/follow")
    public String Subscribe(HttpServletRequest request, @RequestBody TargetProfile targetProfile){

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) request.getAttribute("AuthenticatedUser");
        String userProfileId = authenticatedUser.getProfileId();

        String targetProfileId = targetProfile.getProfileId();

        followService.Subscribe(userProfileId, targetProfileId);
        return "success";
    }

    /**
     * 팔로잉 취소
     */
    @DeleteMapping("/unfollow")
    public String CancelSubscribe(HttpServletRequest request, @RequestParam("targetProfileId") String targetProfileId){

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) request.getAttribute("AuthenticatedUser");
        String userProfileId = authenticatedUser.getProfileId();

        followService.CancelSubscribe(userProfileId, targetProfileId);

        return "success";
    }

    /**
     * 팔로우 유무 확인
     */
    @GetMapping("/follow/isFollowing")
    public IsFollowing IsFollowing(HttpServletRequest request, @RequestParam("targetProfileId") String targetProfileId){

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) request.getAttribute("AuthenticatedUser");
        String userProfileId = authenticatedUser.getProfileId();

        return followService.hasFollowed(userProfileId, targetProfileId);
    }

}
