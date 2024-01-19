package project.booker.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import project.booker.domain.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Report findByReportId(String reportId);

    void deleteByReportId(String reportId);

}
