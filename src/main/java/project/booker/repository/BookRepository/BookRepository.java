package project.booker.repository.BookRepository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.booker.domain.Book;
import project.booker.domain.MemberProfile;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom{

    Book findByBookId(String bookId);

    @EntityGraph(attributePaths = {"memberProfile"})
    Book findFetchByBookId(String bookId);

    void deleteByBookId(String bookId);
}
