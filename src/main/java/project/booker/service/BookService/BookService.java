package project.booker.service.BookService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import project.booker.controller.BookController.dto.*;
import project.booker.domain.Book;

import java.io.IOException;
import java.util.List;

public interface BookService {

    void registerMyBook(String profileId, String isbn13, String img);

    BookDetail searchProgressReports(String profileId, String bookId);

    NewBook isNewBook(String profileId, String isbn13);

    LibraryList getBookList(String profileId, Pageable pageable);

    List<Book> searchReadingBooks(String profileId);

    void changeProgress(ChangeProgress changeProgress);

    void changeSaleSate(ChangeSaleState changeSaleState);

    void deleteBook(String bookId);

    SalePosMemberList searchSaleState(String isbn13) throws IOException;
}
