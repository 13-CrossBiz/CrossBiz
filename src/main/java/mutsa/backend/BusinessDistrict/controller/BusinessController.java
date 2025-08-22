package mutsa.backend.BusinessDistrict.controller;

import lombok.RequiredArgsConstructor;
import mutsa.backend.BusinessDistrict.dto.response.*;
import mutsa.backend.BusinessDistrict.service.BusinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService service;

    // 특정 동(dong)의 원본 레코드 리스트
    @GetMapping("/districts/{dong}")
    public ResponseEntity<List<BusinessDistrictResponse>> list(@PathVariable String dong) {
        return ResponseEntity.ok(service.listByDong(dong));
    }

    // 특정 동(dong)의 업종 분포 (예: [{category:"요식업", count:4}, ...])
    @GetMapping("/districts/{dong}/distribution")
    public ResponseEntity<List<CategoryCountResponse>> distribution(@PathVariable String dong) {
        return ResponseEntity.ok(service.distributionByDong(dong));
    }

    // 특정 동(dong)의 개업/폐업률 (가중 평균) - 원형차트 데이터로 바로 사용 가능
    @GetMapping("/districts/{dong}/ratios")
    public ResponseEntity<RatioResponse> ratios(@PathVariable String dong) {
        return ResponseEntity.ok(service.ratiosByDong(dong));
    }

    // 특정 동(dong)의 개업/폐업률 (백분위 계산결과)
    @GetMapping("/districts/{dong}/ratios/pie")
    public ResponseEntity<RatioPieResponse> ratioPie(@PathVariable String dong) {
        return ResponseEntity.ok(service.ratioPieByDong(dong));
    }


    // 한 번에 내려받기 좋은 요약 (분포 + 개폐업률 + 총점포수)
    @GetMapping("/districts/{dong}/summary")
    public ResponseEntity<BusinessSummaryResponse> summary(@PathVariable String dong) {
        return ResponseEntity.ok(service.summaryByDong(dong));
    }

    // CSV 업로드 (선택)
    @PostMapping("/districts/import")
    public ResponseEntity<String> importCsv(@RequestPart("file") MultipartFile file) {
        int n = service.importCsv(file);
        return ResponseEntity.ok("Imported rows: " + n);
    }
}