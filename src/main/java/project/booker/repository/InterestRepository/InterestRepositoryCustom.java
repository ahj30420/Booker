package project.booker.repository.InterestRepository;

import java.util.List;

public interface InterestRepositoryCustom {

    void bulkSave(Long profilePk, List<String> interestList);

}
