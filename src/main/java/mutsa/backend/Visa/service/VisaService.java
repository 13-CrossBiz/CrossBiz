package mutsa.backend.Visa.service;

import lombok.RequiredArgsConstructor;
import mutsa.backend.Users.repository.UsersRepository;
import mutsa.backend.Visa.dto.request.ai.AiMessage;
import mutsa.backend.Visa.dto.request.ai.AiRequest;
import mutsa.backend.Visa.dto.request.ai.AiResponse;
import mutsa.backend.Visa.dto.request.visa.BasicInfo;
import mutsa.backend.Visa.dto.request.visa.WithVisaInfo;
import mutsa.backend.Visa.dto.request.visa.WithoutVisaInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisaService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final VisaPrompt visaPrompt;
    @Value("${gpt.api.key}")
    private String apiKey;

    @Value("${gpt.url}")
    private String apiUrl;


    public String getPromptWithVisa(BasicInfo basic, WithVisaInfo visa) {
        String prompt= visaPrompt.withVisaPromt(basic, visa);
        return callApi(prompt);
    }

    public String getPromptWithoutVisa(BasicInfo basic, WithoutVisaInfo visa) {
        String prompt= visaPrompt.withoutVisaPromt(basic, visa);
        return callApi(prompt);
    }
    public String callApi(String prompt){
        AiRequest request = new AiRequest();
        request.setModel("gpt-4o");
        request.setTemperature(0.7);
        request.setMessages(List.of(
                new AiMessage("user", prompt)
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        HttpEntity<AiRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<AiResponse> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                AiResponse.class
        );

        return response.getBody()
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}
