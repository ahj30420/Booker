package project.booker.controller.BookController.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BestSeller {

    private String isbn13;
    private String title;
    private String cover;
    private String author;
    private String publisher;
    private String category;
    private String description;

}
