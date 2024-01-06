package project.booker.controller.BookController.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserIsbn13 {

    private String isbn13;

    private Long profileIdx;

}
