package mutsa.backend.Visa.controller;
import lombok.RequiredArgsConstructor;
import mutsa.backend.Visa.dto.request.visa.BasicInfo;
import mutsa.backend.Visa.dto.request.visa.VisaRequestDto;
import mutsa.backend.Visa.dto.request.visa.WithVisaInfo;
import mutsa.backend.Visa.dto.request.visa.WithoutVisaInfo;
import mutsa.backend.Visa.dto.response.NearPlaceDto;
import mutsa.backend.Visa.service.VisaPlaceService;
import mutsa.backend.Visa.service.VisaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/visa")
public class VisaController {
    private final VisaService visaService;
    private final VisaPlaceService visaPlaceService;


    @GetMapping("/place")
    public ResponseEntity<List<NearPlaceDto>> calcululateDistance(
            @RequestParam double lat,
            @RequestParam double lon)
    {
        return ResponseEntity.ok(visaPlaceService.calculateDistance(lat, lon));
    }
    @PostMapping("/recommend/with")
    public ResponseEntity<String> recommendWithVisa(
            @RequestBody VisaRequestDto dto)
    {
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


}
