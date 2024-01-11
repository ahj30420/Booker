package project.booker.service.ReportService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.booker.controller.ReportController.dto.SaveReportDto;
import project.booker.controller.ReportController.dto.UpdateReportDto;
import project.booker.domain.Book;
import project.booker.domain.Enum.Sharing;
import project.booker.domain.Report;
import project.booker.domain.embedded.UploadImg;
import project.booker.repository.BookRepository.BookRepository;
import project.booker.repository.ReportRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{

    private final BookRepository bookRepository;
    private final ReportRepository reportRepository;

    /**
     * 독후감 등록
     */
    @Override
    public void registerReport(SaveReportDto saveReportDto, UploadImg uploadImg) {

        String bookId = saveReportDto.getBookId();
        Book book = bookRepository.findByBookId(bookId);

        String title = saveReportDto.getTitle();
        String content = saveReportDto.getContent();
        Sharing sharing = saveReportDto.getSharing();
        LocalDateTime redate = LocalDateTime.now();

        Report report = Report.createReport(book, title, content, uploadImg, sharing, redate);
        reportRepository.save(report);

    }

    /**
     * 독후감 보기
     */
    public Report viewReport(String reportId){
        return reportRepository.findByReportId(reportId);
    }

    /**
     * 독후감 수정
     * Update_Query Vs Dirty_Checking(변경 감지)
     * 제목, 내용, 이미지, 공개 범위 등 변경 될 수 있는 필드가 여러 개 존재한다.
     * 변경 될 수 있는 모든 경우의 수를 Query 작성하는 것은 비효율적이라 판단되어 변경 감지 활용!
     */
    @Override
    public void updateReport(UpdateReportDto updateReportDto, UploadImg uploadImg) {
        String reportId = updateReportDto.getReportId();
        log.info("reportId={}",reportId);
        Report report = reportRepository.findByReportId(reportId);

        String title = updateReportDto.getTitle();
        String content = updateReportDto.getContent();
        Sharing sharing = updateReportDto.getSharing();
        LocalDateTime redate = LocalDateTime.now();

        report.updateReport(title, content, uploadImg, sharing, redate);
    }

    /**
     * 독후감 삭제
     */
    @Override
    public void deleteReport(String reportId) {
        reportRepository.deleteByReportId(reportId);
    }
}
