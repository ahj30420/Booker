package project.booker.service.AladinAPIService;

import project.booker.controller.BookController.dto.BestSellerList;
import project.booker.controller.BookController.dto.BookInfo;
import project.booker.controller.BookController.dto.SearchBook;
import project.booker.controller.BookController.dto.SearchBookList;

public interface AladinAPIService {
    BestSellerList getBestSeller(String start);

    BookInfo BookLookUp(String ISBN13);

    SearchBookList searchBook(String titleOrAuthor, String size, String maxResult);
}
