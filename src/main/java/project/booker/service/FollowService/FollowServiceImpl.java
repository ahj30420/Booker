package project.booker.service.FollowService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.booker.controller.FollowController.dto.FollowCount;
import project.booker.controller.FollowController.dto.IsFollowing;
import project.booker.domain.Follow;
import project.booker.domain.MemberProfile;
import project.booker.exception.errorcode.ErrorCode;
import project.booker.exception.exceptions.InvalidProfileIdException;
import project.booker.repository.FollowRepository;
import project.booker.repository.PofileRepository.ProfileRepository;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FollowServiceImpl implements  FollowService{

    private final FollowRepository followRepository;
    private final ProfileRepository profileRepository;

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
     */
    @Override
    public List<Follow> searchFollowers(String profileId) {

        MemberProfile memberProfile = profileRepository.findByProfileId(profileId);
        if(memberProfile == null){
            throw new InvalidProfileIdException(ErrorCode.INVALID_PROFILEID);
        }

        List<Follow> follows = followRepository.findAllByFollowing(memberProfile);

        return follows;
    }

    /**
     * 팔로잉 조회
     * 1. profileId 검증
     * 2. 사용자가 팔로잉 하고 있는 팔로우 정보 조회
     */
    @Override
    public List<Follow> searchFollowing(String profileId) {

        MemberProfile memberProfile = profileRepository.findByProfileId(profileId);
        if(memberProfile == null){
            throw new InvalidProfileIdException(ErrorCode.INVALID_PROFILEID);
        }

        List<Follow> follows = followRepository.findAllByFollower(memberProfile);

        return follows;
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
