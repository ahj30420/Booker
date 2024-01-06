package project.booker.service.AladinAPIService;

import project.booker.controller.BookController.dto.BestSeller;
import project.booker.controller.BookController.dto.BookInfo;

import java.util.ArrayList;

public interface AladinAPIService {
    ArrayList<BestSeller> getBestSeller();

    BookInfo BookLookUp(String ISBN13);
}
