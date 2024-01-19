package project.booker.controller.BookController.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.booker.domain.Enum.SaleState;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeSaleState {

    private String bookId;
    private SaleState saleState;

}
