package sn.gerardo.buscaLibroAlura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookDTO(
        @JsonAlias("title") String title,
        @JsonAlias("authors") List<AuthorDTO> authors,
        @JsonAlias("languages") List<String> languages,
        @JsonAlias("download_count") Integer downloadCount
) {
    public String getAuthorNames() {
        return authors != null && !authors.isEmpty()
                ? authors.get(0).name()
                : "Unknown Author";
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
record AuthorDTO(
        @JsonAlias("name") String name
) {}