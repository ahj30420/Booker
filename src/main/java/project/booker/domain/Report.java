package project.booker.domain;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.booker.domain.Enum.Sharing;
import project.booker.domain.embedded.UploadImg;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportPk;

    @Column(unique = true)
    private String reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_pk")
    private Book book;

    private String title;
    private String content;

    @Embedded
    private UploadImg img;

    @Enumerated(EnumType.STRING)
    private Sharing sharing;

    private LocalDateTime redate;

    //----------------------------------------생성 메서드-------------------------------------------------------
    public static Report createReport(Book book, String title, String content, UploadImg img, Sharing sharing, LocalDateTime redate){
        Report report = new Report();
        report.reportId = NanoIdUtils.randomNanoId();
        report.addBook(book);
        report.title = title;
        report.content = content;
        report.img = img;
        report.sharing = sharing;
        report.redate = redate;

        return report;
    }

    //----------------------------------------수정 메서드-------------------------------------------------------
    public void updateReport(String title, String content, UploadImg uploadImg, Sharing sharing, LocalDateTime redate) {
        if(title != null){
            this.title = title;
        }
        if(content != null){
            this.content = content;
        }
        if(uploadImg != null){
            this.img = uploadImg;
        }
        if(sharing != null){
            this.sharing = sharing;
        }
        this.redate = redate;
    }

    //--------------------------------------연관 관계 메서드-----------------------------------------------------
    public void addBook(Book book) {
        this.book = book;
        book.getReports().add(this);
    }

}