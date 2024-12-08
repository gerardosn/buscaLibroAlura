package sn.gerardo.buscaLibroAlura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.gerardo.buscaLibroAlura.model.Book;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitleAndAuthor(String title, String author);
    // Find existing book by title and author
    Optional<Book> findByTitleAndAuthor(String title, String author);
    // New method to list all saved books
    List<Book> findAllByOrderByTitleAsc();
    // get distinct authors
    @Query("SELECT DISTINCT b.author FROM Book b ORDER BY b.author")
    List<String> findDistinctAuthors();
}