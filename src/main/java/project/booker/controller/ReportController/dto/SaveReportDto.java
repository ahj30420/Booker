package project.booker.controller.ReportController.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import project.booker.domain.Enum.Sharing;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveReportDto {

    private String bookId;
    private MultipartFile imageFile;

    @NotBlank
    @Size(max = 30)
    private String title;

    @NotBlank
    @Size(max = 500)
    private String content;

    @NotNull
    private Sharing sharing;

}
