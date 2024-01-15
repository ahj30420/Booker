package project.booker.service.BookService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import project.booker.controller.BookController.dto.BookDetail;
import project.booker.domain.Book;
import project.booker.controller.BookController.dto.NewBook;

import java.util.List;

public interface BookService {

    void registerMyBook(String profileId, String isbn13, String img);

    BookDetail searchProgressReports(String profileId, String bookId);

    NewBook isNewBook(String profileId, String isbn13);

    Slice<Book> getBookList(String profileId, Pageable pageable);

    List<Book> searchReadingBooks(String profileId);

}
