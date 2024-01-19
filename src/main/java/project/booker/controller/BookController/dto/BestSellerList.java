package project.booker.controller.BookController.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BestSellerList {

    private String start;
    private List<BestSeller> bestSellerList;

}
