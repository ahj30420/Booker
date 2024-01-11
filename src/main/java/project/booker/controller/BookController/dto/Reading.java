package project.booker.controller.BookController.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Reading {

    private String bookId;
    private String title;
    private String description;
    private String img;

    @Builder
    public Reading(String bookId, String title, String description, String img) {
        this.bookId = bookId;
        this.title = title;
        this.description = description;
        this.img = img;
    }

}
