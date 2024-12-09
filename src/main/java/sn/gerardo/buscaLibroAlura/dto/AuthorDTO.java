package sn.gerardo.buscaLibroAlura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthorDTO(
        @JsonAlias("name") String name,
        @JsonAlias("birth_year") Integer birthYear,
        @JsonAlias("death_year") Integer deathYear
) {
    // formatted name with years if available
    public String getFormattedName() {
        if (birthYear != null || deathYear != null) {
            String years = String.format("(%s - %s)",
                    birthYear != null ? birthYear : "?",
                    deathYear != null ? deathYear : "?");
            return String.format("%s %s", name, years);
        }
        return name;
    }
}
