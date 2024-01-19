package project.booker.service.AladinAPIService;

import project.booker.controller.BookController.dto.BestSeller;
import project.booker.controller.BookController.dto.BestSellerList;
import project.booker.controller.BookController.dto.BookInfo;

import java.util.ArrayList;

public interface AladinAPIService {
    BestSellerList getBestSeller(String start);

    BookInfo BookLookUp(String ISBN13);
}
