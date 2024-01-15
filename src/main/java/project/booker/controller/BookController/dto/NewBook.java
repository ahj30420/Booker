package project.booker.controller.BookController.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewBook {

    private boolean exist;
    private String bookId;

}
