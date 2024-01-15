package project.booker.domain;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.booker.domain.Enum.Progress;
import project.booker.domain.Enum.SaleState;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookPk;

    @Column(unique = true)
    private String bookId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_pk")
    private MemberProfile memberProfile;

    @OneToMany(mappedBy = "book")
    private List<Report> reports = new ArrayList<>();

    private String isbn13;

    @Enumerated(EnumType.STRING)
    private Progress progress;

    @Enumerated(EnumType.STRING)
    private SaleState saleState;

    private String img;

    //----------------------------------------생성 메서드-------------------------------------------------------
    public static Book createBook(MemberProfile memberProfile, String isbn13, Progress progress, SaleState saleState, String img){
        Book book = new Book();
        book.bookId = NanoIdUtils.randomNanoId();
        book.addProfile(memberProfile);
        book.isbn13 = isbn13;
        book.progress = progress;
        book.saleState = saleState;
        book.img = img;

        return book;
    }

    //--------------------------------------연관 관계 메서드-----------------------------------------------------
    public void addProfile(MemberProfile memberProfile){
        this.memberProfile = memberProfile;
        memberProfile.getBooks().add(this);
    }

}
