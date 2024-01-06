package project.booker.service.BookService;

import java.util.Map;

public interface BookService {

    void registerMyBook(Long profileIdx, String isbn13);

    Map<String, Object> getProgressReport(Long profileIdx, String isbn13);
}
