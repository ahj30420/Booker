package project.booker.controller.BookController.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.booker.domain.Enum.Progress;
import project.booker.domain.Enum.SaleState;

@Data
@NoArgsConstructor
public class BookList {

    private String bookId;
    private String isbn13;
    private Progress progress;
    private SaleState saleState;
    private String img;

    @Builder
    public BookList(String bookId, String isbn13, Progress progress, SaleState saleState, String img) {
        this.bookId = bookId;
        this.isbn13 = isbn13;
        this.progress = progress;
        this.saleState = saleState;
        this.img = img;
    }
}
