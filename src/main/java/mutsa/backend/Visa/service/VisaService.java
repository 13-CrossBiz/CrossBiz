package mutsa.backend.Visa.service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.backend.Users.entity.Users;
import mutsa.backend.Users.repository.UsersRepository;
import mutsa.backend.Visa.dto.request.ai.AiMessage;
import mutsa.backend.Visa.dto.request.ai.AiRequest;
import mutsa.backend.Visa.dto.request.ai.AiResponse;
import mutsa.backend.Visa.dto.request.visa.BasicInfo;
import mutsa.backend.Visa.dto.request.visa.WithVisaInfo;
import mutsa.backend.Visa.dto.request.visa.WithoutVisaInfo;
import mutsa.backend.Visa.dto.response.VisaResponse;
import mutsa.backend.Visa.entity.Visa;
import mutsa.backend.Visa.repository.VisaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class VisaService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final VisaRepository visaRepository;
    private final UsersRepository usersRepository;
    private final VisaPrompt visaPrompt;
    @Value("${gpt.api.key}")
    private String apiKey;

    @Value("${gpt.url}")
    private String apiUrl;


    public String getPromptWithVisa(BasicInfo basic, WithVisaInfo visa) {
        String prompt= visaPrompt.withVisaPromt(basic, visa);
        String result = callApi(prompt);
        visaToDB(parseVisas(result), basic.getUserId());
        return result;
    }

    public String getPromptWithoutVisa(BasicInfo basic, WithoutVisaInfo visa) {
        String prompt= visaPrompt.withoutVisaPromt(basic, visa);
        String result = callApi(prompt);
        visaToDB(parseVisas(result), basic.getUserId());
        return result;
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
        log.info("GPT 응답 결과: {}", response.getBody()
                .getChoices()
                .get(0)
                .getMessage()
                .getContent());
        return response.getBody()
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
    public void visaToDB(List<VisaResponse> visas, Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        List<Visa> visaList = visas.stream()
                .map(dto -> Visa.builder()
                        .name(dto.getName())
                        .visaReason(dto.getReason())
                        .description(dto.getDescription())
                        .warning(dto.getCautions() != null ? String.join(", ", dto.getCautions()) : null)
                        .createdAt(LocalDateTime.now())
                        .user(user)
                        .build())
                .collect(Collectors.toList());
        visaRepository.saveAll(visaList);
    }
    public List<VisaResponse> parseVisas(String json) {
        try {
            String cleanJson = sanitizeJson(json);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(cleanJson);
            JsonNode visaArray = root.path("recommendedVisas");
            return objectMapper.readerFor(new TypeReference<List<VisaResponse>>() {}).readValue(visaArray);
        } catch (Exception e) {
            throw new RuntimeException("recommendedVisas parsing failed", e);
        }
    }
    public String sanitizeJson(String rawJson) {
        if (rawJson == null) return "";
        String cleaned = rawJson.replaceAll("(?i)```json", "")
                .replaceAll("```", "")
                .trim();
        if (cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
            try {
                cleaned = new ObjectMapper().readValue(cleaned, String.class);
            } catch (Exception e) {
            }
        }
        return cleaned;
    }
}
