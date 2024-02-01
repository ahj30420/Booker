package project.booker.controller.BookController.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.booker.domain.Enum.Progress;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDetail {

    private boolean exist;
    private String bookId;
    private String user;
    private Progress progress;
    private List<SimpleReport> simpleReports = new ArrayList<>();

}
