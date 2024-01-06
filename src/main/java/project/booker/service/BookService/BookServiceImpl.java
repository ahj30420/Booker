package project.booker.service.BookService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.booker.domain.Book;
import project.booker.domain.MemberProfile;
import project.booker.domain.Enum.Progress;
import project.booker.domain.Enum.SaleState;
import project.booker.repository.BookRepository;
import project.booker.repository.ProfileRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;
    private final ProfileRepository profileRepository;

    /**
     * 개인 서재에 책 추가하기
     * 독서 현황은 기본 값으로 BEFORE(읽기 전) 설정
     * 판매 가능 여부는 기본 값으로 IMP(불가능) 설정
     */
    @Override
    public void registerMyBook(Long profileIdx, String isbn13) {
        MemberProfile memberProfile = profileRepository.findById(profileIdx).get();
        Book book = Book.createBook(memberProfile, isbn13, Progress.BEFORE, SaleState.IMP);

        bookRepository.save(book);
    }

    /**
     * 책 상세페이지(독서 현황 및 독후감 유무 정보 조회)
     *  1. profileIdx, isbn13로 책 조회
     *  2-1. 책이 없을 경우 false 반환
     *  2-2. 책이 있을 경우 해당 책 정보와 true 값 반환
     */
    @Override
    public Map<String, Object> getProgressReport(Long profileIdx, String isbn13) {

        Book book = bookRepository.getProgress(profileIdx, isbn13);
        log.info("book={}", book);

        Map<String, Object> map = new HashMap<>();
        boolean bookExist = true;
        if(book == null){
            bookExist = false;
            map.put("bookExist", bookExist);
            return map;
        }

        map.put("book", book);
        map.put("bookExist", bookExist);

        return map;
    }

}
