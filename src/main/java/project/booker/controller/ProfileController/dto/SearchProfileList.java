package project.booker.controller.ProfileController.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchProfileList {
    private List<SearchProfile> searchProfileList = new ArrayList<>();
}
