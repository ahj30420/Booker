package project.booker.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.booker.domain.embedded.UploadImg;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_idx")
    private Book book;

    private String title;
    private String content;

    @Embedded
    private UploadImg img;

    private LocalDate redate;

    //----------------------------------------생성 메서드-------------------------------------------------------
    public static Report createReport(Book book, String title, String content, UploadImg img, LocalDate redate){
        Report report = new Report();
        report.addBook(book);
        report.title = title;
        report.content = content;
        report.img = img;
        report.redate = redate;

        return report;
    }

    //--------------------------------------연관 관계 메서드-----------------------------------------------------
    public void addBook(Book book) {
        this.book = book;
        book.getReports().add(this);
    }

}