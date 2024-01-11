package project.booker.repository.BookRepository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import project.booker.controller.BookController.dto.BookList;
import project.booker.controller.BookController.dto.SimpleReport;
import project.booker.domain.Book;
import project.booker.domain.Enum.Sharing;
import project.booker.domain.Report;

import java.util.List;

public interface BookRepositoryCustom {

    List<SimpleReport> searchProgressReports(String bookId, Sharing sharing);

    Book getBookByProfileIdAndIsbn13(String profileId, String isbn13);

    Slice<Book> getBookListByProfileId(String profileId, Pageable pageable);

    List<Book> getReadingBooks(String profileId);

}
