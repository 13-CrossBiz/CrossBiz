package mutsa.backend.Visa.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisaResponse {

    private String name;
    private String reason;
    private String description;
    private List<String> cautions;

}
