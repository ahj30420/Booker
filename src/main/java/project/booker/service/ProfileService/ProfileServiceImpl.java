package project.booker.service.ProfileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.booker.controller.ProfileController.dto.*;
import project.booker.domain.Interest;
import project.booker.domain.Member;
import project.booker.domain.MemberProfile;
import project.booker.domain.embedded.UploadImg;
import project.booker.domain.Enum.Social;
import project.booker.dto.Enum.DefaultImg;
import project.booker.dto.ImgFileDto;
import project.booker.exception.errorcode.ErrorCode;
import project.booker.exception.exceptions.DuplicatedNickNameException;
import project.booker.exception.exceptions.InvalidMemberException;
import project.booker.exception.exceptions.InvalidProfileIdException;
import project.booker.repository.InterestRepository.InterestRepository;
import project.booker.repository.LoginRepository;
import project.booker.repository.PofileRepository.ProfileRepository;
import project.booker.util.ImgStore;
import com.amazonaws.services.s3.AmazonS3;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{

    private final ProfileRepository profileRepository;
    private final LoginRepository loginRepository;
    private final InterestRepository interestRepository;
    private final ImgStore imgStore;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * Member 프로필 등록
     * 1. 닉네임 중복 검사
     * 2. 프로필 등록
     * 3. 소셜(네이버,구글) 회원일 경우와 일반 회원일 경우 구분
     * 4. 소셜 회원일 경우 JWT 발급을 위해 회원정보(idx,이름,닉네임)를 같이 보내준다.
     */
    @Override
    public Map<String, Object> save(String memberId, SaveProfileDto saveProfileDto, UploadImg uploadImg) {

        Map<String, Object> result = new HashMap<>();

        //닉네임 중복 검사
        String nickname = saveProfileDto.getNickname();
        ValidateDuplicateNickName(nickname);

        String intro = saveProfileDto.getIntro();

        Member member = loginRepository.findByMemberId(memberId);

        if(member == null){
            throw new InvalidMemberException(ErrorCode.INVALID_MEMBER);
        }

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
     * 3. API 스팩에 맞게 DTO로 변환
     */
    @Override
    public ViewProfileDto viewProfile(String profileId) throws IOException {
        MemberProfile memberProfile = profileRepository.findFetchInterestByProfileId(profileId);
        if(memberProfile == null){
            throw new InvalidProfileIdException(ErrorCode.INVALID_PROFILEID);
        }

        String nickname = memberProfile.getNickname();
        String intro = memberProfile.getIntro();
        String storeImgName = memberProfile.getImg().getStoreImgName();

        String imgURL = amazonS3.getUrl(bucket,storeImgName).toString();

        List<String> interests = memberProfile.getInterests()
                .stream()
                .map(Interest::getInterest)
                .collect(Collectors.toList());

        ViewProfileDto viewProfileDto = new ViewProfileDto(nickname, intro, imgURL, interests);

        return viewProfileDto;
    }

    /**
     * 프로필 수정
     */
    @Override
    public void updateProfile(String profileId, UpdateProfileDto updateProfileDto) throws IOException {

        MemberProfile profile = profileRepository.findByProfileId(profileId);

        String intro = updateProfileDto.getIntro();

        UploadImg uploadImg = null;
        MultipartFile imgFile = updateProfileDto.getImageFile();
        boolean isDefaultImg = updateProfileDto.isDefaultImg();
        if(imgFile != null || isDefaultImg){
            uploadImg = imgStore.storeImge(imgFile, DefaultImg.PROFILE);
        }

        profile.updateProfile(intro, uploadImg);

        List<String> interestList = updateProfileDto.getInterestList();
        if(interestList != null){
            interestRepository.deleteByMemberProfile(profile);
            interestRepository.bulkSave(profile.getProfilePk(), interestList);
        }
    }

    /**
     * 관심사가 비슷한 유저 추천
     * 1. 관심사가 적어도 절반 이상 같은 사용자 조회
     * 2. API 스팩에 맞춰 DTO로 변환
     */
    @Override
    public RecommendList recommendUser(String profileId, Pageable pageable) throws IOException {

        MemberProfile memberProfile = profileRepository.findFetchInterestByProfileId(profileId);

        List<String> interests = memberProfile.getInterests()
                .stream()
                .map(Interest::getInterest)
                .collect(Collectors.toList());

        Slice<MemberProfile> sliceInfo = profileRepository.searchSimilarUser(memberProfile.getProfilePk(), interests, pageable);

        List<MemberProfile> memberProfiles = sliceInfo.getContent();
        int nowPage = sliceInfo.getNumber();
        boolean hasNext = sliceInfo.hasNext();

        List<RecommendProfileDto> recommends = new ArrayList<>();

        for (MemberProfile profile : memberProfiles) {
            String id = profile.getProfileId();
            String nickname = profile.getNickname();
            String storeImgName = profile.getImg().getStoreImgName();

            String imgURL = amazonS3.getUrl(bucket,storeImgName).toString();

            List<String> interestList = profile.getInterests()
                    .stream()
                    .map(Interest::getInterest)
                    .collect(Collectors.toList());

            RecommendProfileDto recommendProfileDto = RecommendProfileDto.builder()
                    .profileId(id)
                    .nickname(nickname)
                    .imgURL(imgURL)
                    .interests(interestList)
                    .build();

            recommends.add(recommendProfileDto);
        }

        RecommendList recommendList = new RecommendList(nowPage, hasNext, recommends);
        return recommendList;
    }

    /**
     * 유저 검색
     */
    @Override
    public SearchProfileList searchProfile(String nickname) throws IOException {
        List<MemberProfile> memberProfiles = profileRepository.searchProfile(nickname);

        SearchProfileList searchProfileList = new SearchProfileList();
        for (MemberProfile memberProfile : memberProfiles) {
            String profileId = memberProfile.getProfileId();
            String name = memberProfile.getNickname();
            String intro = memberProfile.getIntro();
            String storeImgName = memberProfile.getImg().getStoreImgName();

            String imgURL = amazonS3.getUrl(bucket,storeImgName).toString();

            SearchProfile searchProfile = SearchProfile.builder()
                    .profileId(profileId)
                    .nickname(name)
                    .intro(intro)
                    .imgURL(imgURL)
                    .build();

            searchProfileList.getSearchProfileList().add(searchProfile);
        }

        return searchProfileList;
    }

    /**
     * 프로필 이미지, 닉네임 조회(쪽지 작성 페이지)
     */
    @Override
    public MessageProfile searchSimpleProfile(String profileId) throws IOException {
        MemberProfile profile = profileRepository.findByProfileId(profileId);

        String nickname = profile.getNickname();
        String storeImgName = profile.getImg().getStoreImgName();
        String imgURL = amazonS3.getUrl(bucket,storeImgName).toString();

        MessageProfile messageProfile = MessageProfile.builder()
                .nickname(nickname)
                .imgURL(imgURL)
                .build();

        return messageProfile;
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
