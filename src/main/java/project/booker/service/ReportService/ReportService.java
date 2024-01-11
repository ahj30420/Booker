package project.booker.service.ReportService;

import project.booker.controller.ReportController.dto.SaveReportDto;
import project.booker.controller.ReportController.dto.UpdateReportDto;
import project.booker.domain.Report;
import project.booker.domain.embedded.UploadImg;

public interface ReportService {

    void registerReport(SaveReportDto saveReportDto, UploadImg uploadImg);

    Report viewReport(String reportId);

    void deleteReport(String reportId);

    void updateReport(UpdateReportDto updateReportDto, UploadImg uploadImg);
}
