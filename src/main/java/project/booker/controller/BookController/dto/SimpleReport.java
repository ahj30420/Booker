package project.booker.controller.BookController.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SimpleReport {

    private String reportId;
    private String title;
    private LocalDate redate;

    @QueryProjection
    public SimpleReport(String reportId, String title, LocalDateTime redate) {
        this.reportId = reportId;
        this.title = title;
        this.redate = redate.toLocalDate();
    }
}
