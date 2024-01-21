package project.booker.controller.BookController.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchBookList {

    private List<SearchBook> SearchBooks = new ArrayList<>();

}
