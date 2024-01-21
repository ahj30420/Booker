package project.booker.controller.BookController.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalePosMemberList {

    private List<SalePosMember> salePosMembers = new ArrayList<>();

}
