package project.booker.service.ProfileService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.booker.controller.ProfileController.dto.SaveProfileDto;
import project.booker.controller.ProfileController.dto.ViewProfileDto;
import project.booker.domain.Member;
import project.booker.domain.MemberProfile;
import project.booker.domain.embedded.Interest;
import project.booker.domain.embedded.UploadImg;
import project.booker.domain.Enum.Social;
import project.booker.exception.errorcode.ErrorCode;
import project.booker.exception.exceptions.DuplicatedNickNameException;
import project.booker.exception.exceptions.InvalidProfileIdException;
import project.booker.repository.LoginRepository;
import project.booker.repository.ProfileRepository;
import project.booker.util.ImgStore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{

    private final ProfileRepository profileRepository;
    private final LoginRepository loginRepository;

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

        Interest interest = new Interest(saveProfileDto.getInterest1(), saveProfileDto.getInterest2(), saveProfileDto.getInterest3(), saveProfileDto.getInterest4(), saveProfileDto.getInterest5());

        Member member = loginRepository.findByMemberId(memberId);
        MemberProfile memberProfile = MemberProfile.createMemberProfile(member, nickname, intro, uploadImg, interest);

        profileRepository.save(memberProfile);

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
        MemberProfile memberProfile = profileRepository.findByProfileId(profileId);
        if(memberProfile == null){
            throw new InvalidProfileIdException(ErrorCode.INVALID_PROFILEID);
        }

        return memberProfile;
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
