package project.booker.controller.BookController.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookInfo {

    private String isbn13;
    private String title;
    private String author;
    private String publisher;
    private LocalDate pubDate;
    private String cover;
    private String category;
    private String description;

}
