package sn.gerardo.buscaLibroAlura.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.gerardo.buscaLibroAlura.dto.BookDTO;
import sn.gerardo.buscaLibroAlura.model.Book;
import sn.gerardo.buscaLibroAlura.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public List<SaveResult> saveBooks(List<BookDTO> bookDTOs) {
        List<SaveResult> saveResults = new ArrayList<>();

        for (BookDTO dto : bookDTOs) {
            SaveResult result = saveBook(dto);
            saveResults.add(result);
        }

        return saveResults;
    }

    @Transactional
    public SaveResult saveBook(BookDTO dto) {
        // Check if book already exists
        boolean exists = bookRepository.existsByTitleAndAuthor(dto.title(), dto.getAuthorNames());

        if (exists) {
            return new SaveResult(dto, false, "El libro ya existe en la base de datos");
        }

        // Convert and save new book
        Book book = convertToEntity(dto);
        bookRepository.save(book);
        return new SaveResult(dto, true, "Libro guardado exitosamente");
    }

    private Book convertToEntity(BookDTO dto) {
        Book book = new Book();
        book.setTitle(dto.title());
        book.setAuthor(dto.getAuthorNames());
        book.setLanguages(dto.languages());
        book.setDownloadCount(dto.downloadCount());
        // Set author birth and death years from the first author in the list
        book.setAuthorBirthYear(dto.getFirstAuthorBirthYear());
        book.setAuthorDeathYear(dto.getFirstAuthorDeathYear());
        return book;
    }

    // Result class to provide detailed feedback about saving
    public static class SaveResult {
        public final BookDTO book;
        public final boolean saved;
        public final String message;

        public SaveResult(BookDTO book, boolean saved, String message) {
            this.book = book;
            this.saved = saved;
            this.message = message;
        }
    }
}