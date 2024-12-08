package sn.gerardo.buscaLibroAlura.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.gerardo.buscaLibroAlura.dto.BookDTO;
import sn.gerardo.buscaLibroAlura.model.Book;
import sn.gerardo.buscaLibroAlura.repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public void saveBooks(List<BookDTO> bookDTOs) {
        List<Book> booksToSave = bookDTOs.stream()
                .filter(dto -> !bookRepository.existsByTitleAndAuthor(dto.title(), dto.getAuthorNames()))
                .map(this::convertToEntity)
                .collect(Collectors.toList());

        bookRepository.saveAll(booksToSave);
    }

    private Book convertToEntity(BookDTO dto) {
        Book book = new Book();
        book.setTitle(dto.title());
        book.setAuthor(dto.getAuthorNames());
        book.setLanguages(dto.languages());
        book.setDownloadCount(dto.downloadCount());
        return book;
    }
}