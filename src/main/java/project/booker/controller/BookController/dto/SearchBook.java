package project.booker.controller.BookController.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchBook {

    private String isbn13;
    private String title;
    private String author;
    private String publisher;
    private String img;

    @Builder
    public SearchBook(String isbn13, String title, String author, String publisher, String img) {
        this.isbn13 = isbn13;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.img = img;
    }
}
