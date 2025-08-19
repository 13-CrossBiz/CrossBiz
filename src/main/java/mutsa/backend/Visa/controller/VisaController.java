package mutsa.backend.Visa.controller;
import lombok.RequiredArgsConstructor;
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

}
