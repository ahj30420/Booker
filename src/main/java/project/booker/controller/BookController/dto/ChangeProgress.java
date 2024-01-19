package project.booker.controller.BookController.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.booker.domain.Enum.Progress;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeProgress {

    private String bookId;
    private Progress progress;

}
