package project.booker.controller.BookController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.booker.controller.BookController.dto.*;
import project.booker.domain.Book;
import project.booker.domain.Report;
import project.booker.dto.AuthenticatedUser;
import project.booker.exception.exceptions.ValidationException;
import project.booker.service.AladinAPIService.AladinAPIService;
import project.booker.service.BookService.BookService;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookController {

    private final AladinAPIService aladinAPIService;
    private final MessageSource messageSource;
    private final BookService bookService;

    /**
     * 알라딘 베스트 셀러 조회
     */
    @GetMapping("/book/bestseller")
    public ArrayList<BestSeller> bestSeller(){
        return aladinAPIService.getBestSeller();
    }

    /**
     * 알라딘 책 상세 정보 조회
     */
    @GetMapping("/book")
    public BookInfo bookLookUp(@RequestParam("ISBN13") String isbn13){
        return aladinAPIService.BookLookUp(isbn13);
    }

    /**
     * 개인 서재에 책 추가하기
     * 1.AccessToken에서 profileIdx 추출
     * 2.profileIdx, isbn13으로 책 추가
     */
    @PostMapping("/book/mybook")
    public void registerMyBook(HttpServletRequest request, @RequestBody Isbn13 isbn13){

        AuthenticatedUser user = (AuthenticatedUser) request.getAttribute("AuthenticatedUser");
        Long profileIdx = user.getIdx();

        bookService.registerMyBook(profileIdx, isbn13.getIsbn13());
    }

    /**
     * 책 상세페이지(독서 현황 및 독후감 유무 정보 조회)
     *
     * <유의 사항>
     *     - 책 상세 페이지 종류
     *     1. 회원이 "책 추가"하지 않은 새로운 책
     *     2. 회원이 "책 추가"하여 관리하고 있는 책
     *     3. 로그인 된 회원이 아닌 다른 회원의 개인 서재 책
     * </>
     *
     * <로직 흐름>
     *     1. 회원이 관리하고 있는 책인지 다른 회원의 개인 서재 책인지 구분하기 위해 profileIdx 설정
     *     2. profileIdx와 isbn13 값으로 책 조회
     *     3-1. 책이 존재하지 않을 경우 false값 반환
     *     3-2. 책이 존재 할 경우 해당 책의 독서 현황, 독후감 정보, true 값을 반환
     * </>
     */
    @GetMapping("/book/progress/reports")
    public Map<String, Object> getProgressReports(HttpServletRequest request, @ModelAttribute UserIsbn13 userIsbn13){

        Long profileIdx = userIsbn13.getProfileIdx();

        if(profileIdx == null) {
            AuthenticatedUser user = (AuthenticatedUser) request.getAttribute("AuthenticatedUser");
            profileIdx = user.getIdx();
        }

        Map<String, Object> bookStateMap = bookService.getProgressReport(profileIdx, userIsbn13.getIsbn13());

        if(!(boolean)bookStateMap.get("bookExist")){
            return bookStateMap;
        }

        Map<String, Object> result = new HashMap<>();
        BookState bookState = new BookState();

        Book book = (Book) bookStateMap.get("book");
        bookState.setProgress(book.getProgress());
        List<Report> reports = book.getReports();

        for(int i = 0; i < reports.size(); i++){
            SimpleReport simpleReport = new SimpleReport();
            simpleReport.setTitle(reports.get(i).getTitle());
            simpleReport.setRedate(reports.get(i).getRedate());
            bookState.getReports().add(simpleReport);
        }

        result.put("bookState", bookState);
        result.put("bookExist", bookStateMap.get("bookExist"));

        return result;
    }

}
