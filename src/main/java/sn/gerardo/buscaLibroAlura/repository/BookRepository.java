package sn.gerardo.buscaLibroAlura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.gerardo.buscaLibroAlura.model.Book;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitleAndAuthor(String title, String author);
    // Find existing book by title and author
    Optional<Book> findByTitleAndAuthor(String title, String author);

}