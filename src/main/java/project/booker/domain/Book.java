package project.booker.domain;

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
    private Long bookIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_idx")
    private MemberProfile memberProfile;

    @OneToMany(mappedBy = "book")
    private List<Report> reports = new ArrayList<>();

    private String isbn13;

    @Enumerated(EnumType.STRING)
    private Progress progress;

    @Enumerated(EnumType.STRING)
    private SaleState saleState;

    //----------------------------------------생성 메서드-------------------------------------------------------
    public static Book createBook(MemberProfile memberProfile, String isbn13, Progress progress, SaleState saleState){
        Book books = new Book();
        books.addProfile(memberProfile);
        books.isbn13 = isbn13;
        books.progress = progress;
        books.saleState = saleState;

        return books;
    }

    //--------------------------------------연관 관계 메서드-----------------------------------------------------
    public void addProfile(MemberProfile memberProfile){
        this.memberProfile = memberProfile;
        memberProfile.getBooks().add(this);
    }

    //----------------------------------------비즈니스 로직------------------------------------------------------

}
