package project.booker.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project.booker.domain.embedded.UploadImg;
import project.booker.dto.Enum.DefaultImg;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class ImgStore {

    @Value("${img.dir}")
    private String ImgDir;

    /**
     * 이미지 파일에서 실제 이미지 이름과 DB에 저장할 이미지 이름 추출
     * 저장소에 같은 이미지 이름이 있다면 덮어씌어 질 수 있기때문에 실제 이름과 저장할 때 이름을 분리해야 된다.
     */
    public UploadImg storeImge(MultipartFile imgFile, DefaultImg defaultImg) throws IOException {

        //이미지가 없다면 기본 이미지로 등록
        if(imgFile == null) {
            if (defaultImg == DefaultImg.PROFILE){
                return new UploadImg("DefaultProfile.jpg", "DefaultProfile.jpg");
            } else if (defaultImg == DefaultImg.REPORT){
                return new UploadImg("DefaultReport.png", "DefaultReport.png");
            }
        }

        String originalFilename = imgFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        imgFile.transferTo(new File(getFullPath(storeFileName)));

        return new UploadImg(originalFilename, storeFileName);
    }

    /**
     * 파일을 저장할 경로 설정
     */
    public String getFullPath(String storeFileName) {
        return ImgDir + storeFileName;
    }

    /**
     * MiMe타입 반환하기
     */
    public String getMimeType(String storeFileName){
        String ext = extractExt(storeFileName);
        return "image/"+ext;
    }

    //--------------------------------------Private Method-----------------------------------------------------

    /**
     * 원본 파일의 확장자로 저장할 파일 이름 생성
     * 저장될 파일 이름은 중복되지 않게 UUID로 생성하였습니다.
     */
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    /**
     * 원본 파일의 확장자명 추출
     */
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos+1);
    }

}
