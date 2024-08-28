package springframework.spring_ai_intro.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record GetCapitalWithInfo(@JsonPropertyDescription("This is the name of the city.") String name,
                                 @JsonPropertyDescription("This is the region of the country or geographical region of the city.") String region,
                                 @JsonPropertyDescription("This is the population of the city.") int population,
                                 @JsonPropertyDescription("This is the official language used in the city") String language,
                                 @JsonPropertyDescription("This is the official currency that is used in the city") String currency) {
}
