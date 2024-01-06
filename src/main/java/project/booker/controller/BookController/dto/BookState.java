package project.booker.controller.BookController.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.booker.domain.Enum.Progress;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookState {

    private Progress progress;
    private List<SimpleReport> reports = new ArrayList<>();

}
