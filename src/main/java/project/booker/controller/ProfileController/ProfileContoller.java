package project.booker.controller.ProfileController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.booker.controller.ProfileController.dto.SaveProfileDto;
import project.booker.controller.ProfileController.dto.ViewProfileDto;
import project.booker.domain.Enum.Social;
import project.booker.domain.MemberProfile;
import project.booker.domain.embedded.UploadImg;
import project.booker.dto.AuthenticatedUser;
import project.booker.dto.Enum.DefaultImg;
import project.booker.exception.exceptions.ValidationException;
import project.booker.service.LoginService.LoginService;
import project.booker.service.ProfileService.ProfileService;
import project.booker.util.ImgStore;
import project.booker.util.jwt.Jwt;
import project.booker.util.jwt.JwtProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProfileContoller {

    private final ProfileService profileService;
    private final LoginService loginService;
    private final MessageSource messageSource;
    private final ImgStore imgStore;
    private final JwtProvider jwtProvider;

    /**
     * Member 프로필 등록
     * 1. 프로필 입력값 검증
     * 2. 실제 이미지 이름, 저장용 이미지 이름 분리
     * 3. 프로필 등록
     * 4-1. 일반 회원일 경우 login 페이지로 이동 시켜 로그인 하게 한다.
     * 4-2. 소셜(네이버,구글) 회원일 경우 JWT를 발급하여 로그인 처리해준다.
     */
    @PostMapping("/profile")
    public Map<String, String> registerProfile(@RequestParam("memberId") String memberId,
                                  @Validated @ModelAttribute SaveProfileDto saveProfileDto,
                                  BindingResult bindingResult) throws IOException {

        //프로필 입력값 검증
        if(bindingResult.hasErrors()){
            sendValidationError(bindingResult);
        }

        log.info("memberId={}", memberId);
        MultipartFile ImgFile = saveProfileDto.getImageFile();
        UploadImg uploadImg = imgStore.storeImge(ImgFile, DefaultImg.PROFILE);

        Map<String, Object> socialMap = profileService.save(memberId, saveProfileDto, uploadImg);
        Map<String, String> result = new HashMap<>();

        if(socialMap.get("social") == Social.NORMAL){
            result.put("social", "NORMARL");
            result.put("redirect","/login");
            return result;
        }

        Long memberPk = (Long)socialMap.get("memberPk");
        Jwt jwt = createJwt(socialMap);
        loginService.UpdateRefreshToken(memberPk, jwt.getRefreshToken());

        result.put("accessToken", jwt.getAccessToken());
        result.put("refreshToken", jwt.getRefreshToken());

        return result;
    }

    /**
     * 프로필 정보 조회
     * @Param profileId: 사용자의 프로필인지 타인의 프로필인지 구분하기 위해 사용
     *
     * 1. profileId로 누구의 프로필 조회인지 구분
     * 2. response Dto에 프로필 정보 매핑
     */
    @GetMapping("/profileInfo")
    public ViewProfileDto viewProfile(HttpServletRequest request,
                            @RequestParam(name = "profileId", required = false) String profileId) throws IOException {

        if(profileId == null){
            AuthenticatedUser user = (AuthenticatedUser) request.getAttribute("AuthenticatedUser");
            profileId = user.getProfileId();
        }

        MemberProfile memberProfile = profileService.viewProfile(profileId);

        String nickname = memberProfile.getNickname();
        String intro = memberProfile.getIntro();
        String storeImgName = memberProfile.getImg().getStoreImgName();
        String interest1 = memberProfile.getInterest().getInterest1();
        String interest2 = memberProfile.getInterest().getInterest2();
        String interest3 = memberProfile.getInterest().getInterest3();
        String interest4 = memberProfile.getInterest().getInterest4();
        String interest5 = memberProfile.getInterest().getInterest5();

        File imgFile = new File(imgStore.getFullPath(storeImgName));
        byte[] imgBytes = Files.readAllBytes(imgFile.toPath());
        String base64Image = Base64.getEncoder().encodeToString(imgBytes);
        String mimeType = imgStore.getMimeType(storeImgName);

        List<String> interets = new ArrayList<>(Arrays.asList(interest1, interest2, interest3, interest4, interest5))
                .stream()
                .filter(val -> val != null)
                .toList();

        ViewProfileDto viewProfileDto = new ViewProfileDto(nickname, intro, base64Image, mimeType, interets);

        return viewProfileDto;
    }




    //--------------------------------------Private Method-----------------------------------------------------

    /**
     * 검증 실패시 오류 메시지를 담아 Exception을 발생 시켜주는 함수
     */
    private void sendValidationError(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for(FieldError error : bindingResult.getFieldErrors()){
            String message = messageSource.getMessage(error, Locale.getDefault());
            errors.put(error.getField(),message);
        }
        throw new ValidationException(errors);
    }

    /**
     * JWT 발급하기
     */
    private Jwt createJwt(Map<String, Object> socialMap) {
        String profileId = (String) socialMap.get("profileId");
        String name = (String) socialMap.get("name");
        String nickname = (String) socialMap.get("nickname");
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(profileId, name, nickname);

        Map<String,Object> claims = new HashMap<>();
        claims.put("AuthenticetedUser", authenticatedUser);

        Jwt jwt = jwtProvider.createJwt(claims);

        return jwt;
    }

}
