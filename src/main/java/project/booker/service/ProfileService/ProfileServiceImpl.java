package project.booker.service.ProfileService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.booker.controller.ProfileController.dto.SaveProfileDto;
import project.booker.domain.Interest;
import project.booker.domain.Member;
import project.booker.domain.MemberProfile;
import project.booker.domain.embedded.UploadImg;
import project.booker.domain.Enum.Social;
import project.booker.exception.errorcode.ErrorCode;
import project.booker.exception.exceptions.DuplicatedNickNameException;
import project.booker.exception.exceptions.InvalidProfileIdException;
import project.booker.repository.InterestRepository.InterestRepository;
import project.booker.repository.LoginRepository;
import project.booker.repository.PofileRepository.ProfileRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{

    private final ProfileRepository profileRepository;
    private final LoginRepository loginRepository;
    private final InterestRepository interestRepository;

    /**
     * Member 프로필 등록
     * 1. 닉네임 중복 검사
     * 2. 프로필 등록
     * 3. 소셜(네이버,구글) 회원일 경우와 일반 회원일 경우 구분
     * 4. 소셜 회원일 경우 JWT 발급을 위해 회원정보(idx,이름,닉네임)를 같이 보내준다.
     */
    @Override
    @Transactional
    public Map<String, Object> save(String memberId, SaveProfileDto saveProfileDto, UploadImg uploadImg) {

        Map<String, Object> result = new HashMap<>();

        //닉네임 중복 검사
        String nickname = saveProfileDto.getNickname();
        ValidateDuplicateNickName(nickname);

        String intro = saveProfileDto.getIntro();

        Member member = loginRepository.findByMemberId(memberId);
        MemberProfile memberProfile = MemberProfile.createMemberProfile(member, nickname, intro, uploadImg);

        profileRepository.save(memberProfile);

        List<String> interestList = saveProfileDto.getInterestList();
        interestRepository.bulkSave(memberProfile.getProfilePk(), interestList);

        if(member.getSocial() == Social.NORMAL){
            result.put("social", member.getSocial());
            return result;
        }

        result.put("social", member.getSocial());
        result.put("memberPk", member.getMemberPk());
        result.put("profileId", memberProfile.getProfileId());
        result.put("name", member.getName());
        result.put("nickname", nickname);

        return result;
    }

    /**
     * 프로필 정보 조회
     * 1. profileId로 프로필 정보 조회
     * 2. 프로필 정보가 없을 경우 잘못된 profileId 예외 처리
     */
    @Override
    public MemberProfile viewProfile(String profileId) {
        MemberProfile memberProfile = profileRepository.findFetchInterestByProfileId(profileId);
        if(memberProfile == null){
            throw new InvalidProfileIdException(ErrorCode.INVALID_PROFILEID);
        }

        return memberProfile;
    }

    /**
     * 관심사가 비슷한 유저 추천
     * 관심사가 적어도 절반 이상 같은 사용자 조회
     */
    @Override
    public Slice<MemberProfile> recommendUser(String profileId, Pageable pageable) {

        MemberProfile memberProfile = profileRepository.findFetchInterestByProfileId(profileId);

        List<String> interests = memberProfile.getInterests()
                .stream()
                .map(Interest::getInterest)
                .collect(Collectors.toList());

        return profileRepository.searchSimilarUser(memberProfile.getProfilePk(), interests, pageable);
    }
    //--------------------------------------Private Method-----------------------------------------------------

    /**
     * 프로필 닉네임 중복 체크 메소드
     */
    private void ValidateDuplicateNickName(String nickname) {
        Optional.ofNullable(profileRepository.findProfileByNickname(nickname))
                .ifPresent(user ->{
                    throw new DuplicatedNickNameException(ErrorCode.DUPLICATED_PROFILE_NICKNAME);
                });
    }

}
