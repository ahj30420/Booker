package project.booker.service.BookService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.booker.controller.BookController.dto.*;
import project.booker.domain.Book;
import project.booker.domain.Enum.Sharing;
import project.booker.domain.MemberProfile;
import project.booker.domain.Enum.Progress;
import project.booker.domain.Enum.SaleState;
import project.booker.domain.embedded.UploadImg;
import project.booker.dto.ImgFileDto;
import project.booker.exception.errorcode.ErrorCode;
import project.booker.exception.exceptions.NotExistBookException;
import project.booker.repository.BookRepository.BookRepository;
import project.booker.repository.PofileRepository.ProfileRepository;
import project.booker.util.ImgStore;
import com.amazonaws.services.s3.AmazonS3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;
    private final ProfileRepository profileRepository;
    private final ImgStore imgStore;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 개인 서재에 책 추가하기
     * 독서 현황은 기본 값으로 BEFORE(읽기 전) 설정
     * 판매 가능 여부는 기본 값으로 IMP(불가능) 설정
     */
    @Override
    public void registerMyBook(String profileId, String isbn13, String img) {

        MemberProfile memberProfile = profileRepository.findByProfileId(profileId);
        Book book = Book.createBook(memberProfile, isbn13, Progress.BEFORE, SaleState.IMP, img);

        bookRepository.save(book);
    }

    /**
     * 책 상세페이지(독서 현황 및 독후감 유무 정보 조회)
     *
     * 1. bookId로 책 정보 조회
     * 2. 조회 된 책이 없다면 개인 서재에 해당 책이 없다는 예외 처리
     * 3-1. 조회된 책의 주인이 현재 사용자와 같지 않다면 타인의 개인 서재 방문으로 간주하여 공개 여부가 PUBLIC인 독후감만 조회
     * 3-2. 조회된 책의 주인이 현재 사용자와 같다면 공개 여부와 상관없이 모든 독후감 조회
     * 4. 책의 존재 여부, 독서 현황, 독후감 정보 반환
     */
    @Override
    public BookDetail searchProgressReports(String profileId, String bookId) {

        Book book = bookRepository.findFetchByBookId(bookId);

        if(book == null){ throw new NotExistBookException(ErrorCode.NotExist_Book); }

        Sharing sharing = null;
        if(!book.getMemberProfile().getProfileId().equals(profileId)){
            sharing = Sharing.PUBLIC;
        }

        List<SimpleReport> reports = bookRepository.searchProgressReports(bookId, sharing);

        BookDetail bookDetail = new BookDetail(true, bookId, book.getProgress(), reports);

        return bookDetail;
    }

    /**
     * 새로운 책인지 개인 서재에 등록된 책인지 파악
     *
     * 1. profileId + isbn13으로 책 조회
     * 2-1. 책이 존재 하지 않다면 새로운 책으로 간주하고 exist = false 반환
     * 2-2. 책이 존재하면 exist = true 와 BookId 반환
     */
    @Override
    public NewBook isNewBook(String profileId, String isbn13) {

        Book book = bookRepository.getBookByProfileIdAndIsbn13(profileId, isbn13);

        NewBook newBook = null;
        if(book == null){
            newBook = new NewBook(false, null);
            return newBook;
        }

        newBook = new NewBook(true, book.getBookId());
        return newBook;
    }

    /**
     * 개인 서재 목록 조회
     * 1. 해당 사용자의 개인 서재 책 목록 조회
     * 2. Dto로 변환 및 반환
     */
    @Override
    public LibraryList getBookList(String profileId, Pageable pageable) {
        Slice<Book> sliceInfo = bookRepository.getBookListByProfileId(profileId, pageable);
        List<Book> books = sliceInfo.getContent();
        int nowPage = sliceInfo.getNumber();
        boolean hasNext = sliceInfo.hasNext();

        List<BookList> bookLists = new ArrayList<>();
        for(int i = 0; i < books.size(); i++){
            BookList bookList = BookList.builder()
                    .bookId(books.get(i).getBookId())
                    .isbn13(books.get(i).getIsbn13())
                    .progress(books.get(i).getProgress())
                    .saleState(books.get(i).getSaleState())
                    .img(books.get(i).getImg())
                    .build();

            bookLists.add(bookList);
        }

        LibraryList libraryList = new LibraryList(nowPage, hasNext, bookLists);
        return libraryList;
    }

    /**
     * 읽는 중인 책 목록 조회
     * 1. 사용자가 읽고 있는 책 목록 조회
     */
    @Override
    public List<Book> searchReadingBooks(String profileId) {
        return bookRepository.getReadingBooks(profileId);
    }

    /**
     * 독서 현황 변경
     */
    @Override
    public void changeProgress(ChangeProgress changeProgress) {
        String bookId = changeProgress.getBookId();
        Progress progress = changeProgress.getProgress();

        Book book = bookRepository.findByBookId(bookId);
        book.changeProgress(progress);
    }

    /**
     * 거래 가능 여부 변경
     */
    @Override
    public void changeSaleSate(ChangeSaleState changeSaleState) {
        String bookId = changeSaleState.getBookId();
        SaleState saleState = changeSaleState.getSaleState();

        log.info("bookId={}", bookId);
        Book book = bookRepository.findByBookId(bookId);
        book.changeSaleState(saleState);
    }

    /**
     * 책 삭제 하기
     */
    @Override
    public void deleteBook(String bookId) {
        bookRepository.deleteByBookId(bookId);
    }

    /**
     * 책 거래 기능
     * 설명: 요청받은 책을 거래 가능한 유저 검색(SaleSate == POS)
     */
    @Override
    public SalePosMemberList searchSaleState(String isbn13) throws IOException {
        List<Book> books = bookRepository.findAllFetchByIsbn13AndSaleState(isbn13, SaleState.POS);

        SalePosMemberList salePosMemberList = new SalePosMemberList();
        for (Book book : books) {
            String profileId = book.getMemberProfile().getProfileId();
            String nickname = book.getMemberProfile().getNickname();
            String storeImgName = book.getMemberProfile().getImg().getStoreImgName();

            String imgURL = amazonS3.getUrl(bucket,storeImgName).toString();

            SalePosMember member = SalePosMember.builder()
                    .profileId(profileId)
                    .imgURL(imgURL)
                    .nickname(nickname)
                    .build();

            salePosMemberList.getSalePosMembers().add(member);
        }

        return salePosMemberList;
    }
}