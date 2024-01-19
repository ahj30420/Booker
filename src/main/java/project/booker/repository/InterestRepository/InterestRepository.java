package project.booker.repository.InterestRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.booker.domain.Interest;

public interface InterestRepository extends JpaRepository<Interest, Long>, InterestRepositoryCustom {

}
