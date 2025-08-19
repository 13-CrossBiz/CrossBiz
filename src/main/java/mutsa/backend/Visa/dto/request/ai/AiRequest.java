package mutsa.backend.Visa.dto.request.ai;

import lombok.Data;

import java.util.List;

@Data
public class AiRequest {

    private String model;
    private double temperature;
    private List<AiMessage> messages;
}
