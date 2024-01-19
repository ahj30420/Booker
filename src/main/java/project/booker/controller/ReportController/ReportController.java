package project.booker.controller.ReportController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.booker.controller.ReportController.dto.SaveReportDto;
import project.booker.controller.ReportController.dto.UpdateReportDto;
import project.booker.domain.Enum.Sharing;
import project.booker.domain.Report;
import project.booker.domain.embedded.UploadImg;
import project.booker.dto.AuthenticatedUser;
import project.booker.dto.Enum.DefaultImg;
import project.booker.dto.ImgFileDto;
import project.booker.exception.exceptions.ValidationException;
import project.booker.service.ReportService.ReportService;
import project.booker.util.ImgStore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final MessageSource messageSource;
    private final ImgStore imgStore;

    /**
     * 독후감 등록하기
     * 1. 제목과 내용 입력값 검증
     * 2. 실제 이미지 이름, 저장용 이미지 이름 분리
     * 3. 등록
     */
    @PostMapping("/report")
    public void registerReport(@Validated @ModelAttribute SaveReportDto saveReportDto, BindingResult bindingResult) throws IOException {

        if(bindingResult.hasErrors()){
            sendValidationError(bindingResult);
        }

        MultipartFile imgFile = saveReportDto.getImageFile();
        UploadImg uploadImg = imgStore.storeImge(imgFile, DefaultImg.REPORT);

        reportService.registerReport(saveReportDto, uploadImg);
    }

    /**
     * 독후감 보기
     * 1. reportId로 해당 독후감 정보 조회
     * 2. 이미지 전송을 위해 파일을 Byte 배열로 쪼개고 base64로 인코딩한다.
     * 3. 프론트에서 이를 이미지 파일로 읽는데 필요한 MIME_Type도 같이 전송해준다.
     */
    @GetMapping("/report")
    public Map<String,Object> viewReport(@RequestParam("reportId") String reportId) throws IOException {
        Report report = reportService.viewReport(reportId);

        String title = report.getTitle();
        String content = report.getContent();
        Sharing sharing = report.getSharing();
        LocalDateTime redate = report.getRedate();

        String storeImgName = report.getImg().getStoreImgName();
        ImgFileDto imgFile = imgStore.getImgFile(storeImgName);

        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("content", content);
        map.put("sharing", sharing);
        map.put("redate", redate);
        map.put("imgBytes", imgFile.getBase64Image());
        map.put("mimeType", imgFile.getMimeType());

        return map;

    }

    /**
     * 독후감 수정하기
     * 1. 독후감 변경 내용을 전송 받는다.
     * 2. 제목, 내용이 변경되었으면 입력 값을 검증한다.
     * 3-1. 이미지가 변경되었는지 확인 한다.
     * 3-2. 이미지를 바꾸지 않아도 ImageFile 값이 null이 되고
     *      이미지를 기본 이미지로 바꾸기 위해 기존 이미지를 삭제했어도 ImageFile이 null로 매핑된다.
     *      따라서 이를 구분해 주기위해 Boolean 값인 DefaultImg를 같이 받는다.(DefaultImg = true이면 기본 이미지로 변경 / DefaultImg = false이면 이미지 변경 X)
     * 4. 위 내용을 바탕으로 변경된 부분을 업데이트 한다.
     */
    @PatchMapping("/report")
    public void updateReport(@Validated @ModelAttribute UpdateReportDto updateReportDto, BindingResult bindingResult) throws IOException {

        if(bindingResult.hasErrors()){
            sendValidationError(bindingResult);
        }

        UploadImg uploadImg = null;
        MultipartFile imgFile = updateReportDto.getImageFile();
        boolean isDefaultImg = updateReportDto.isDefaultImg();
        if(imgFile != null || isDefaultImg){
            uploadImg = imgStore.storeImge(imgFile, DefaultImg.REPORT);
        }

        reportService.updateReport(updateReportDto, uploadImg);
    }

    /**
     * 독후감 삭제하기
     */
    @DeleteMapping("/report")
    public void deleteReport(@RequestParam("reportId") String reportId){
        reportService.deleteReport(reportId);
    }

    //--------------------------------------Private Method-----------------------------------------------------

    /**
     * 검증 실패시 오류 메시지를 담아 Exception을 발생 시켜주는 함수
     */
    private void sendValidationError(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for(FieldError error : bindingResult.getFieldErrors()){
            String message = messageSource.getMessage(error, Locale.getDefault());
            errors.put(error.getField(), message);
        }
        throw new ValidationException(errors);
    }
}
