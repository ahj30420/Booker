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
import project.booker.controller.ProfileController.dto.request.ProfileDto;
import project.booker.controller.ProfileController.dto.request.UploadImgDto;
import project.booker.dto.AuthenticatedUser;
import project.booker.exception.exceptions.ValidationException;
import project.booker.service.ProfileService.ProfileService;
import project.booker.util.ImgStore;

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
    private final MessageSource messageSource;
    private final ImgStore imgStore;

    /**
     * Member 프로필 등록
     */
    @PostMapping
    public String RegisterProfile(@RequestParam("idx") Long memberIdx,
                                  @Validated @ModelAttribute ProfileDto profileDto,
                                  BindingResult bindingResult) throws IOException {

        //프로필 입력값 검증
        if(bindingResult.hasErrors()){
            sendValidationError(bindingResult);
        }

        MultipartFile ImgFile = profileDto.getImageFile();
        UploadImgDto uploadImgDto = imgStore.storeImge(ImgFile);

        profileService.save(memberIdx, profileDto, uploadImgDto);

        return "profile success";
    }


    //Member 프로필 이미지 다운로드
    @GetMapping("/img/downloadImg")
    public Resource downloadImage(HttpServletRequest request) throws MalformedURLException {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) request.getAttribute("AuthenticatedUser");
        Long MemberIdx = authenticatedUser.getIdx();
        String storeImgName = profileService.getStoreImgName(MemberIdx);
        log.info("MemberIdx={}", MemberIdx);
        return new UrlResource("file:" + imgStore.getFullPath(storeImgName));
    }


//--------------------------------------Private Method-----------------------------------------------------

/*
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

}
