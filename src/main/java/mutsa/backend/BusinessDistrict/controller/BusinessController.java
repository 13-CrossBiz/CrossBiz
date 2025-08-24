package mutsa.backend.BusinessDistrict.controller;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import mutsa.backend.BusinessDistrict.dto.BusinessGrade;
import mutsa.backend.BusinessDistrict.dto.ppl.*;
import mutsa.backend.BusinessDistrict.dto.sales.BusinessRankResponse;
import mutsa.backend.BusinessDistrict.repository.BusinessPPlRepository;
import mutsa.backend.BusinessDistrict.service.BusinessService;
import org.springframework.web.bind.annotation.*;
import mutsa.backend.BusinessDistrict.dto.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/business")
@SecurityRequirement(name = "BearerAuth")
public class BusinessController {
    private final BusinessService businessService;
    private final BusinessPPlRepository repo;

    /**
     * 점포수
     */
    @GetMapping("/districts/{dong}")
    public ResponseEntity<List<BusinessDistrictResponse>> list(@PathVariable String dong) {
        return ResponseEntity.ok(businessService.listByDong(dong));
    }

    // 특정 동(dong)의 업종 분포 (예: [{category:"요식업", count:4}, ...])
    @GetMapping("/districts/{dong}/distribution")
    public ResponseEntity<List<CategoryCountResponse>> distribution(@PathVariable String dong) {
        return ResponseEntity.ok(businessService.distributionByDong(dong));
    }

    // 특정 동(dong)의 개업/폐업률 (가중 평균) - 원형차트 데이터로 바로 사용 가능
    @GetMapping("/districts/{dong}/ratios")
    public ResponseEntity<RatioResponse> ratios(@PathVariable String dong) {
        return ResponseEntity.ok(businessService.ratiosByDong(dong));
    }

    // 특정 동(dong)의 개업/폐업률 (백분위 계산결과)
    @GetMapping("/districts/{dong}/ratios/pie")
    public ResponseEntity<RatioPieResponse> ratioPie(@PathVariable String dong) {
        return ResponseEntity.ok(businessService.ratioPieByDong(dong));
    }


    // 한 번에 내려받기 좋은 요약 (분포 + 개폐업률 + 총점포수)
    @GetMapping("/districts/{dong}/summary")
    public ResponseEntity<BusinessSummaryResponse> summary(@PathVariable String dong) {
        return ResponseEntity.ok(businessService.summaryByDong(dong));
    }

    // CSV 업로드 (선택)
    @PostMapping("/districts/import")
    public ResponseEntity<String> importCsv(@RequestPart("file") MultipartFile file) {
        int n = businessService.importCsv(file);
        return ResponseEntity.ok("Imported rows: " + n);
    }

    /**
     * 매출
     */
    @GetMapping // 각 동의 매출 n순위 업종
    public BusinessRankResponse getRankByQuery(@RequestParam String dong, @RequestParam int rank) {
        return businessService.getRank(dong, rank);
    }
    @GetMapping("/dong/{dong}")// 각 동의 업종을 내림차순으로 limit개
    public List<BusinessRankResponse> getTopNByDong(@PathVariable String dong,
                                                    @RequestParam(defaultValue = "6") int limit) {
        return businessService.getAll(dong, limit);
    }
    /**
     * 유동인구
     */
    @GetMapping("/people/topn")
    public List<BusinessTopn> getTopNDongAndTotalPpl() {
        return repo.findTopnTotalPplPerDong();
    }
    @GetMapping("/people/gender")
    public BusinessGender getGender(@RequestParam String dong){
        return  businessService.getGender(dong);
    }
    @GetMapping("/people/age")
    public BusinessAge getAge(@RequestParam String dong){
        return  businessService.getAge(dong);
    }
    @GetMapping("/people/time")
    public BusinessTime getTime(@RequestParam String dong){
        return businessService.getTime(dong);
    }
    @GetMapping("/people/day")
    public BusinessDay getDay(@RequestParam String dong){
        return businessService.getDay(dong);
    }
    @GetMapping("/people/quarter")
    public BusinessQuarter getQuarter(@RequestParam String dong) {return businessService.getQuarter(dong);}
    @GetMapping("/grade")
    public BusinessGrade getGrade (@RequestParam String dong){
        return businessService.getGrade(dong);
    }
}