package project.booker.controller.BookController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import project.booker.controller.BookController.dto.*;
import project.booker.domain.Book;
import project.booker.dto.AuthenticatedUser;
import project.booker.dto.NewBook;
import project.booker.service.AladinAPIService.AladinAPIService;
import project.booker.service.BookService.BookService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final AladinAPIService aladinAPIService;
    private final BookService bookService;

    /**
     * 알라딘 베스트 셀러 조회
     */
    @GetMapping("/bestseller")
    public ArrayList<BestSeller> bestSeller(){
        return aladinAPIService.getBestSeller();
    }

    /**
     * 알라딘 책 상세 정보 조회
     */
    @GetMapping
    public BookInfo bookLookUp(@RequestParam("ISBN13") String isbn13){
        return aladinAPIService.BookLookUp(isbn13);
    }

    /**
     * 개인 서재에 책 추가하기
     * 1.AccessToken에서 profileIdx 추출
     * 2.profileIdx, isbn13으로 책 추가
     */
    @PostMapping("/mybook")
    public void registerMyBook(HttpServletRequest request, @RequestBody SaveBook saveBook){

        AuthenticatedUser user = (AuthenticatedUser) request.getAttribute("AuthenticatedUser");
        String profileId = user.getProfileId();

        bookService.registerMyBook(profileId, saveBook.getIsbn13(), saveBook.getImg());
    }

    /**
     * 책 상세페이지(독서 현황 및 독후감 유무 정보 조회)
     * 1. 사용자의 profileId 값과 입력 받은 bookId 값으로 독서현황 + 독후감 정보 조회
     */
    @GetMapping("/progress/reports")
    public BookDetail searchProgressReports(HttpServletRequest request, @RequestParam("bookId") String bookId){

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) request.getAttribute("AuthenticatedUser");
        String profileId = authenticatedUser.getProfileId();

        BookDetail bookDetail = bookService.searchProgressReports(profileId, bookId);

        return bookDetail;
    }

    /**
     * 검색을 통한 책 상세페이지 (새로운 책인지 개인 서재에 등록된 책인지 파악)
     *
     * 1. 사용자의 profileId와 isbn13 값으로 해당 책이 개인 서재에서 관리되고 있는지 확인
     * 2-1. 책이 개인 서재에서 관리되고 있다면 해당 정보로 독서 현황 + 독후감 정보 조회 및 반환
     * 2-2. 개인 서재에서 관리되지 않는 새로운 책이라면 독서 현황 + 독후감 정보 없음을 반환
     */
    @GetMapping("/newbook")
    public BookDetail isNewBook(HttpServletRequest request, @RequestParam("isbn13") String isbn13){

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) request.getAttribute("AuthenticatedUser");
        String profileId = authenticatedUser.getProfileId();

        NewBook newBook = bookService.isNewBook(profileId, isbn13);

        BookDetail bookDetail = null;
        boolean exist = newBook.isExist();
        if(exist){
            String bookId = newBook.getBookId();;
            bookDetail = bookService.searchProgressReports(profileId, bookId);
            return bookDetail;
        }

        bookDetail = new BookDetail(exist, null, null);
        return bookDetail;
    }


    /**
     * 개인 서재 책 목록 조회
     *
     * @param profileId = 타인의 개인 서재 방문할 때 사용
     * 무한 스크롤링을 위해 Slice 형식의 페이징 처리
     *
     * 1. 사용자의 개인 서재인지 타인의 개인 서재인지 구분
     * 2. 개인 서재의 책 목록 조회
     * 3. 책의 ISBN13 값으로 책 정보 조회(알라딘 API)
     * 4. BookID + 책 이미지 반환
     */
    @GetMapping("/library/list")
    public LibraryList searchBookList(HttpServletRequest request,
                                        @PageableDefault(page = 0, size = 5) Pageable pageable,
                                        @RequestParam(name="profileId", required = false) String profileId){

        if(profileId == null) {
            AuthenticatedUser authenticatedUser = (AuthenticatedUser) request.getAttribute("AuthenticatedUser");
            profileId = authenticatedUser.getProfileId();
        }

        Slice<Book> sliceInfo = bookService.getBookList(profileId, pageable);
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
     * 읽고 있는 책 목록 조회 (메인 페이지)
     * 1. profileId로 사용자가 읽고 있는 책 목록 조회
     * 2. API 형식에 맞춰 DTO로 변환
     */
    @GetMapping("/reading")
    public ReadingList searchReadingBooks(HttpServletRequest request){

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) request.getAttribute("AuthenticatedUser");
        String profileId = authenticatedUser.getProfileId();
        String userName = authenticatedUser.getName();

        List<Book> books = bookService.searchReadingBooks(profileId);
        List<Reading> readings = new ArrayList<>();

        for (int i = 0; i < books.size(); i++){
            String isbn13 = books.get(i).getIsbn13();
            BookInfo bookInfo = aladinAPIService.BookLookUp(isbn13);

            Reading reading = Reading.builder()
                    .bookId(books.get(i).getBookId())
                    .title(bookInfo.getTitle())
                    .description(bookInfo.getDescription())
                    .img(books.get(i).getImg())
                    .build();

            readings.add(reading);
        }

        ReadingList readingList = new ReadingList(userName, readings);
        return readingList;
    }

}