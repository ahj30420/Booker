package project.booker.service.LibraryAPIService;

import project.booker.controller.BookController.dto.Librarys;

public interface LibraryAPIService {
    Librarys libSrchByBook(String isbn13, String region);
}
