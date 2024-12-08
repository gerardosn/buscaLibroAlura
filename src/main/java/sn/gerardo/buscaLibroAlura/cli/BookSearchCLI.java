package sn.gerardo.buscaLibroAlura.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.gerardo.buscaLibroAlura.dto.BookDTO;
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

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Buscador de Libros ---");
            System.out.print("Ingrese término de búsqueda (o 'salir' para terminar): ");
            String searchTerm = scanner.nextLine();

            if ("salir".equalsIgnoreCase(searchTerm)) {
                break;
            }

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
        scanner.close();
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
}