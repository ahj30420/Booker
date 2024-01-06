package project.booker.controller.ProfileController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.booker.controller.ProfileController.dto.ProfileDto;
import project.booker.controller.ProfileController.dto.UploadImgDto;
import project.booker.domain.Enum.Social;
import project.booker.dto.AuthenticatedUser;
import project.booker.exception.exceptions.ValidationException;
import project.booker.service.LoginService.LoginService;
import project.booker.service.ProfileService.ProfileService;
import project.booker.util.ImgStore;
import project.booker.util.jwt.Jwt;
import project.booker.util.jwt.JwtProvider;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
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
    @PostMapping
    public Map<String, String> RegisterProfile(@RequestParam("idx") Long memberIdx,
                                  @Validated @ModelAttribute ProfileDto profileDto,
                                  BindingResult bindingResult) throws IOException {

        //프로필 입력값 검증
        if(bindingResult.hasErrors()){
            sendValidationError(bindingResult);
        }

        MultipartFile ImgFile = profileDto.getImageFile();
        UploadImgDto uploadImgDto = imgStore.storeImge(ImgFile);

        Map<String, Object> socialMap = profileService.save(memberIdx, profileDto, uploadImgDto);
        Map<String, String> result = new HashMap<>();

        if(socialMap.get("social") == Social.NORMAL){
            result.put("social", "NORMARL");
            result.put("redirect","/login");
            return result;
        }

        Jwt jwt = createJwt(socialMap);
        loginService.UpdateRefreshToken(memberIdx, jwt.getRefreshToken());

        result.put("accessToken", jwt.getAccessToken());
        result.put("refreshToken", jwt.getRefreshToken());

        return result;
    }


    /**
     * Member 프로필 이미지 다운로드
     */
    @GetMapping("/img/downloadImg")
    public Resource downloadImage(HttpServletRequest request) throws MalformedURLException {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) request.getAttribute("AuthenticatedUser");
        Long MemberIdx = authenticatedUser.getIdx();
        String storeImgName = profileService.getStoreImgName(MemberIdx);
        return new UrlResource("file:" + imgStore.getFullPath(storeImgName));
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
        Long idx = (Long) socialMap.get("idx");
        String name = (String) socialMap.get("name");
        String nickname = (String) socialMap.get("nickname");
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(idx, name, nickname);

        Map<String,Object> claims = new HashMap<>();
        claims.put("AuthenticetedUser", authenticatedUser);

        Jwt jwt = jwtProvider.createJwt(claims);

        return jwt;
    }

}
