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
    // New method to get the first author's birth year
    public Integer getFirstAuthorBirthYear() {
        return authors != null && !authors.isEmpty()
                ? authors.get(0).birthYear()
                : null;
    }

    // New method to get the first author's death year
    public Integer getFirstAuthorDeathYear() {
        return authors != null && !authors.isEmpty()
                ? authors.get(0).deathYear()
                : null;
    }
}