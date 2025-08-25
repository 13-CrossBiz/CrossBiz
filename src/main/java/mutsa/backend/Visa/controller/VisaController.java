package mutsa.backend.Visa.controller;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import mutsa.backend.Visa.dto.request.visa.*;
import mutsa.backend.Visa.dto.response.NearPlaceDto;
import mutsa.backend.Visa.dto.response.VisaHistory;
import mutsa.backend.Visa.entity.Visa;
import mutsa.backend.Visa.repository.VisaRepository;
import mutsa.backend.Visa.service.VisaPlaceService;
import mutsa.backend.Visa.service.VisaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/visa")
@SecurityRequirement(name = "BearerAuth")
public class VisaController {
    private final VisaService visaService;
    private final VisaPlaceService visaPlaceService;
    private final VisaRepository visaRepository;

    @GetMapping("/place")
    public ResponseEntity<List<NearPlaceDto>> calcululateDistance(
            @RequestParam double lat,
            @RequestParam double lon) {
        return ResponseEntity.ok(visaPlaceService.calculateDistance(lat, lon));
    }

    @PostMapping("/recommend/with")
    public ResponseEntity<String> recommendWithVisa(
            @RequestBody VisaRequestDto dto) {
        BasicInfo basic = dto.getBasicInfo();
        WithVisaInfo visa = dto.getWithVisaInfo();
        String prompt = visaService.getPromptWithVisa(basic, visa);
        return ResponseEntity.ok(prompt);
    }

    @PostMapping("/recommend/without")
    public ResponseEntity<String> recommendWithoutVisa(
            @RequestBody VisaRequestDto dto
    ) {
        BasicInfo basic = dto.getBasicInfo();
        WithoutVisaInfo visa = dto.getWithoutVisaInfo();
        String prompt = visaService.getPromptWithoutVisa(basic, visa);
        return ResponseEntity.ok(prompt);
    }

    @GetMapping("/history")
    public ResponseEntity<List<VisaHistory>> visaHistory(@RequestParam Long userId) {
        List<Visa> visas = visaRepository.findByUser_UserId(userId);
        List<VisaHistory> result = visas.stream()
                .map(visa -> VisaHistory.builder()
                        .name(visa.getName())
                        .createdAt(visa.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}