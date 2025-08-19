package mutsa.backend.Visa.dto.request.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiResponse {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private AiMessage message;
    }
}