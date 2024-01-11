package project.booker.repository.BookRepository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import project.booker.controller.BookController.dto.QSimpleReport;
import project.booker.controller.BookController.dto.SimpleReport;
import project.booker.domain.Book;
import project.booker.domain.Enum.Progress;
import project.booker.domain.Enum.Sharing;
import project.booker.domain.QBook;

import java.util.List;

import static project.booker.domain.QBook.*;
import static project.booker.domain.QMemberProfile.*;
import static project.booker.domain.QReport.*;

public class BookRepositoryImpl implements BookRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public BookRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 독후감 조회(with 동적 쿼리)
     *
     * <동적 쿼리>
     *     1. sharing = null 일 경우
     *     - bookIdx에 해당하는 모든 독후감 조회
     *
     *     2. sharing != null 일 경우
     *     - bookIdx에 해당하면서 공개 여부(Sharing = PUBLIC)인 독후감 조회
     * </>
     */
    @Override
    public List<SimpleReport> searchProgressReports(String bookId, Sharing sharing) {

        List<SimpleReport> SimpleReports = queryFactory
                .select(new QSimpleReport(
                        report.reportId,
                        report.title,
                        report.redate
                ))
                .from(report)
                .where(report.book.bookId.eq(bookId),
                        sharingEq(sharing)
                )
                .fetch();

        return SimpleReports;
    }

    /**
     * profileId + isbn13에 해당하는 책 조회
     */
    @Override
    public Book getBookByProfileIdAndIsbn13(String profileId, String isbn13) {
        Book book = queryFactory.selectFrom(QBook.book)
                .join(QBook.book.memberProfile, memberProfile)
                .where(memberProfile.profileId.eq(profileId),
                        QBook.book.isbn13.eq(isbn13)
                )
                .fetchOne();

        return book;
    }

    /**
     * 프로필 Id로 해당 회원이 관리하는 개인 서재 책 목록 조회
     * 1. 무한 스크롤링을 위해 Slice 형식의 페이징 처리
     */
    @Override
    public Slice<Book> getBookListByProfileId(String profileId, Pageable pageable) {

        int pageSize = pageable.getPageSize();

        List<Book> books = queryFactory
                .selectFrom(book)
                .join(book.memberProfile, memberProfile)
                .where(memberProfile.profileId.eq(profileId))
                .offset(pageable.getOffset())
                .limit(pageSize + 1)
                .fetch();

        boolean hasNext = false;
        if(books.size() > pageSize){
            books.remove(pageSize);
            hasNext = true;
        }

        return new SliceImpl<>(books, pageable, hasNext);
    }

    /**
     * 읽고 있는 책 목록 조회
     */
    @Override
    public List<Book> getReadingBooks(String profileId) {

        List<Book> books = queryFactory.selectFrom(book)
                .join(book.memberProfile, memberProfile)
                .where(memberProfile.profileId.eq(profileId),
                        book.progress.eq(Progress.READING)
                )
                .fetch();

        return books;
    }
    //--------------------------------------Private Method-----------------------------------------------------

    /**
     * 동적 쿼리 메소드
     *  - sharing이 존재하면 "report.sharing = PUBLIC" 조건 추가
     */
    private BooleanExpression sharingEq(Sharing sharing) {
        return sharing == null ? null : report.sharing.eq(Sharing.PUBLIC);
    }

}
