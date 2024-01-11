package project.booker.controller.ReportController.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import project.booker.domain.Enum.Sharing;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReportDto {

    private String reportId;

    private MultipartFile imageFile;

    private boolean defaultImg;

    @Size(max= 30)
    private String title;

    @Size(max = 500)
    private String content;

    private Sharing sharing;

}
