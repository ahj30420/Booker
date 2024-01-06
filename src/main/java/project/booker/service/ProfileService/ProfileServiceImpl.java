package project.booker.service.ProfileService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.booker.controller.ProfileController.dto.ProfileDto;
import project.booker.controller.ProfileController.dto.UploadImgDto;
import project.booker.domain.Member;
import project.booker.domain.MemberProfile;
import project.booker.domain.embedded.Interest;
import project.booker.domain.embedded.UploadImg;
import project.booker.domain.Enum.Social;
import project.booker.exception.errorcode.ErrorCode;
import project.booker.exception.exceptions.DuplicatedNickNameException;
import project.booker.repository.LoginRepository;
import project.booker.repository.ProfileRepository;
import project.booker.util.jwt.JwtProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{

    private final ProfileRepository profileRepository;
    private final LoginRepository loginRepository;
    private final JwtProvider jwtProvider;

    /**
     * Member 프로필 등록
     * 1. 닉네임 중복 검사
     * 2. 프로필 등록
     * 3. 소셜(네이버,구글) 회원일 경우와 일반 회원일 경우 구분
     * 4. 소셜 회원일 경우 JWT 발급을 위해 회원정보(idx,이름,닉네임)를 같이 보내준다.
     */
    @Override
    @Transactional
    public Map<String, Object> save(Long memberIdx, ProfileDto profileDto, UploadImgDto uploadImgDto) {

        Map<String, Object> result = new HashMap<>();

        //닉네임 중복 검사
        String nickname = profileDto.getNickname();
        ValidateDuplicateNickName(nickname);

        String intro = profileDto.getIntro();

        Interest interest = new Interest(profileDto.getInterest1(), profileDto.getInterest2(), profileDto.getInterest3(), profileDto.getInterest4(), profileDto.getInterest5());
        UploadImg uploadImg = new UploadImg(uploadImgDto.getRealImgName(), uploadImgDto.getStoreImgName());

        Member member = loginRepository.findById(memberIdx).get();
        MemberProfile memberProfile = MemberProfile.createMemberProfile(member, nickname, intro, uploadImg, interest);

        profileRepository.save(memberProfile);

        if(member.getSocial() == Social.NORMAL){
            result.put("social", member.getSocial());
            return result;
        }

        result.put("social", member.getSocial());
        result.put("idx", memberProfile.getProfileIdx());
        result.put("name", member.getName());
        result.put("nickname", nickname);

        return result;
    }

    /**
     * 저장된 프로필 이미지 가져오기
     */
    @Transactional(readOnly = true)
    public String getStoreImgName(Long MemberIdx) {
        MemberProfile memberProfile = profileRepository.findById(MemberIdx).get();
        return memberProfile.getImg().getStoreImgName();
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
