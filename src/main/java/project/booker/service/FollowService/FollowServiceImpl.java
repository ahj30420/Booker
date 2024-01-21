package project.booker.service.FollowService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.booker.controller.FollowController.dto.FollowCount;
import project.booker.controller.FollowController.dto.FollowProfile;
import project.booker.controller.FollowController.dto.IsFollowing;
import project.booker.domain.Follow;
import project.booker.domain.MemberProfile;
import project.booker.dto.ImgFileDto;
import project.booker.exception.errorcode.ErrorCode;
import project.booker.exception.exceptions.InvalidProfileIdException;
import project.booker.repository.FollowRepository;
import project.booker.repository.PofileRepository.ProfileRepository;
import project.booker.util.ImgStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FollowServiceImpl implements  FollowService{

    private final FollowRepository followRepository;
    private final ProfileRepository profileRepository;
    private final ImgStore imgStore;

    /**
     * 팔로우 수 조회
     * 1. profileId 검증
     * 2. 팔로워 / 팔로잉 수 조회
     * 3. FollowCount Dto로 반환
     */
    @Override
    public FollowCount searchFollowCount(String profileId) {

        MemberProfile memberProfile = profileRepository.findByProfileId(profileId);
        if(memberProfile == null){
            throw new InvalidProfileIdException(ErrorCode.INVALID_PROFILEID);
        }

        Long countFollower = followRepository.countByFollowing(memberProfile);
        Long countFollowing = followRepository.countByFollower(memberProfile);

        FollowCount followCount = new FollowCount(countFollower, countFollowing);

        return followCount;
    }

    /**
     * 팔로워 조회
     * 1. profileId 검증
     * 2. 사용자를 팔로잉 하고 있는 팔로우 정보 조회
     * 3. API 스팩에 맞게 DTO로 변환
     */
    @Override
    public List<FollowProfile> searchFollowers(String profileId) throws IOException {

        MemberProfile memberProfile = profileRepository.findByProfileId(profileId);
        if(memberProfile == null){
            throw new InvalidProfileIdException(ErrorCode.INVALID_PROFILEID);
        }

        List<Follow> followList = followRepository.findAllByFollowing(memberProfile);
        List<FollowProfile> result = new ArrayList<>();

        for (Follow follow : followList) {
            String userProfileId = follow.getFollower().getProfileId();
            String nickname = follow.getFollower().getNickname();
            String intro = follow.getFollower().getIntro();
            String storeImgName = follow.getFollower().getImg().getStoreImgName();

            ImgFileDto imgFile = imgStore.getImgFile(storeImgName);

            FollowProfile followProfile = FollowProfile.builder()
                    .profileId(userProfileId)
                    .nickname(nickname)
                    .intro(intro)
                    .imgFile(imgFile)
                    .build();

            result.add(followProfile);
        }

        return result;
    }

    /**
     * 팔로잉 조회
     * 1. profileId 검증
     * 2. 사용자가 팔로잉 하고 있는 팔로우 정보 조회
     * 3. API 스팩에 맞게 DTO로 변환
     */
    @Override
    public List<FollowProfile> searchFollowing(String profileId) throws IOException {

        MemberProfile memberProfile = profileRepository.findByProfileId(profileId);
        if(memberProfile == null){
            throw new InvalidProfileIdException(ErrorCode.INVALID_PROFILEID);
        }

        List<Follow> followList = followRepository.findAllByFollower(memberProfile);
        List<FollowProfile> result = new ArrayList<>();

        for (Follow follow : followList) {
            String userProfileId = follow.getFollowing().getProfileId();
            String nickname = follow.getFollowing().getNickname();
            String intro = follow.getFollowing().getIntro();
            String storeImgName = follow.getFollowing().getImg().getStoreImgName();

            ImgFileDto imgFile = imgStore.getImgFile(storeImgName);
            FollowProfile followProfile = FollowProfile.builder()
                    .profileId(userProfileId)
                    .nickname(nickname)
                    .intro(intro)
                    .imgFile(imgFile)
                    .build();

            result.add(followProfile);
        }

        return result;
    }

    /**
     * 팔로잉 하기
     * 1. profileId 검증
     * 2. 팔로우 추가
     */
    @Override
    public void Subscribe(String userProfileId, String targetProfileId) {

        MemberProfile userProfile = profileRepository.findByProfileId(userProfileId);
        MemberProfile targetProfile = profileRepository.findByProfileId(targetProfileId);
        if(targetProfile == null){
            throw new InvalidProfileIdException(ErrorCode.INVALID_PROFILEID);
        }

        Follow follow = Follow.createFollow(userProfile, targetProfile);
        followRepository.save(follow);
    }

    /**
     * 팔로잉 취소
     * 1. profileId 검증
     * 2. 팔로우 삭제
     */
    @Override
    public void CancelSubscribe(String userProfileId, String targetProfileId) {

        MemberProfile userProfile = profileRepository.findByProfileId(userProfileId);
        MemberProfile targetProfile = profileRepository.findByProfileId(targetProfileId);
        if(targetProfile == null){
            throw new InvalidProfileIdException(ErrorCode.INVALID_PROFILEID);
        }

        followRepository.deleteByFollowerAndFollowing(userProfile, targetProfile);
    }

    /**
     * 팔로잉 유무 확인
     */
    @Override
    public IsFollowing hasFollowed(String userProfileId, String targetProfileId) {

        MemberProfile userProfile = profileRepository.findByProfileId(userProfileId);
        MemberProfile targetProfile = profileRepository.findByProfileId(targetProfileId);
        if(targetProfile == null){
            throw new InvalidProfileIdException(ErrorCode.INVALID_PROFILEID);
        }

        boolean hasFollowed = true;
        Follow follow = followRepository.findByFollowerAndFollowing(userProfile, targetProfile);
        if(follow == null){
            hasFollowed = false;
        }

        IsFollowing isFollowing = new IsFollowing(hasFollowed);

        return isFollowing;
    }
}
