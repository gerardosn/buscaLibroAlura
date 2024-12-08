package sn.gerardo.buscaLibroAlura.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sn.gerardo.buscaLibroAlura.dto.BookDTO;

import java.util.List;
import java.util.stream.Collectors;

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

            return objectMapper.readerForListOf(BookDTO.class)
                    .readValue(resultsNode);
        } catch (Exception e) {
            throw new RuntimeException("Error searching books", e);
        }
    }
}