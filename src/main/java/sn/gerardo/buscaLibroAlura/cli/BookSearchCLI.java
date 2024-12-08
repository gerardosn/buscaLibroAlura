package sn.gerardo.buscaLibroAlura.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.gerardo.buscaLibroAlura.dto.BookDTO;
import sn.gerardo.buscaLibroAlura.model.Book;
import sn.gerardo.buscaLibroAlura.repository.BookRepository;
import sn.gerardo.buscaLibroAlura.service.BookService;
import sn.gerardo.buscaLibroAlura.service.GutendexService;

import java.util.List;
import java.util.Scanner;

@Component
public class BookSearchCLI implements CommandLineRunner {
    @Autowired
    private GutendexService gutendexService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMainMenu();

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    searchBooksMenu(scanner);
                    break;
                case "2":
                    listSavedBooks();
                    break;
                case "3":
                    listSavedAuthors();
                    break;
                case "000":
                    System.out.println("Saliendo del programa...");
                    return;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n--- Menú Principal ---");
        System.out.println("1. Buscar libros en Gutendex");
        System.out.println("2. Listar libros guardados");
        System.out.println("3. Listar autores guardados");
        System.out.println("000. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private void searchBooksMenu(Scanner scanner) {
        System.out.print("Ingrese término de búsqueda: ");
        String searchTerm = scanner.nextLine();

        List<BookDTO> books = gutendexService.searchBooks(searchTerm);
        displayBooks(books);

        if (!books.isEmpty()) {
            System.out.print("¿Desea guardar los libros encontrados? (s/n): ");
            String saveChoice = scanner.nextLine();
            if ("s".equalsIgnoreCase(saveChoice)) {
                saveAndDisplayResults(books);
            }
        }
    }

    private void listSavedBooks() {
        List<Book> savedBooks = bookRepository.findAllByOrderByTitleAsc();

        if (savedBooks.isEmpty()) {
            System.out.println("No hay libros guardados en la base de datos.");
            return;
        }

        System.out.println("\n--- Libros Guardados ---");
        for (int i = 0; i < savedBooks.size(); i++) {
            Book book = savedBooks.get(i);
            System.out.printf("%d. %s\n", i + 1, book.getTitle());
        }

        System.out.printf("\nTotal de libros guardados: %d\n", savedBooks.size());
    }

    private void displayBooks(List<BookDTO> books) {
        if (books.isEmpty()) {
            System.out.println("No se encontraron libros.");
            return;
        }

        System.out.println("\n--- Resultados de la Búsqueda ---");
        for (int i = 0; i < books.size(); i++) {
            BookDTO book = books.get(i);
            System.out.printf("%d. Título: %s\n", i + 1, book.title());
            System.out.printf("   Autor: %s\n", book.getAuthorNames());
            System.out.printf("   Idiomas: %s\n", String.join(", ", book.languages()));
            System.out.printf("   Descargas: %d\n\n", book.downloadCount());
        }
    }

    private void saveAndDisplayResults(List<BookDTO> books) {
        System.out.println("\n--- Resultados de Guardado ---");
        int savedCount = 0;
        int existingCount = 0;

        List<BookService.SaveResult> saveResults = bookService.saveBooks(books);

        for (BookService.SaveResult result : saveResults) {
            System.out.println("Libro: " + result.book.title());
            System.out.println("Estado: " + result.message);

            if (result.saved) {
                savedCount++;
            } else {
                existingCount++;
            }
            System.out.println("---");
        }

        System.out.printf("\nResumen: %d libros guardados, %d libros existentes\n",
                savedCount, existingCount);
    }

    private void listSavedAuthors() {
        List<String> savedAuthors = bookRepository.findDistinctAuthors();

        if (savedAuthors.isEmpty()) {
            System.out.println("No hay autores guardados en la base de datos.");
            return;
        }

        System.out.println("\n--- Autores Guardados ---");
        for (int i = 0; i < savedAuthors.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, savedAuthors.get(i));
        }

        System.out.printf("\nTotal de autores guardados: %d\n", savedAuthors.size());
    }
}