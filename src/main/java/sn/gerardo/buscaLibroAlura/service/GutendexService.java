package sn.gerardo.buscaLibroAlura.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sn.gerardo.buscaLibroAlura.dto.AuthorDTO;
import sn.gerardo.buscaLibroAlura.dto.BookDTO;


import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;


@Service
public class GutendexService {
    private static final String API_URL = "https://gutendex.com/books/?search=";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<BookDTO> searchBooks(String query) {
        String url = API_URL + query.replace(" ", "+");
        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode resultsNode = rootNode.get("results");
//            utilizando BookDTO.class
//            return objectMapper.readerForListOf(BookDTO.class)
//                    .readValue(resultsNode);
            List<BookDTO> books = new ArrayList<>();

            for (JsonNode bookNode : resultsNode) {
                // Extract book details
                String title = bookNode.get("title").asText();
                JsonNode authorsNode = bookNode.get("authors");
                List<AuthorDTO> authors = new ArrayList<>();

                // Extract author information including birth and death years
                for (JsonNode authorNode : authorsNode) {
                    String authorName = authorNode.get("name").asText();

                    // Handle potential null values for birth and death years
                    Integer birthYear = authorNode.has("birth_year") && !authorNode.get("birth_year").isNull()
                            ? authorNode.get("birth_year").asInt()
                            : null;

                    Integer deathYear = authorNode.has("death_year") && !authorNode.get("death_year").isNull()
                            ? authorNode.get("death_year").asInt()
                            : null;

                    authors.add(new AuthorDTO(authorName, birthYear, deathYear));
                }

                // Extract languages and download count
                List<String> languages = new ArrayList<>();
                for (JsonNode langNode : bookNode.get("languages")) {
                    languages.add(langNode.asText());
                }

                Integer downloadCount = bookNode.has("download_count")
                        ? bookNode.get("download_count").asInt()
                        : 0;

                // Create BookDTO with full information
                BookDTO bookDTO = new BookDTO(title, authors, languages, downloadCount);
                books.add(bookDTO);
            }

            return books;
        } catch (Exception e) {
            throw new RuntimeException("Error searching books", e);
        }
    }
}