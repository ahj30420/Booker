package project.booker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.booker.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b left join fetch b.reports where b.isbn13 = :isbn13 and b.memberProfile.profileIdx = :profileIdx")
    public Book getProgress(@Param("profileIdx") Long profileIdx, @Param("isbn13") String isbn13);

}
