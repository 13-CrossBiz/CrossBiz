package mutsa.backend.BusinessDistrict.controller;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import mutsa.backend.BusinessDistrict.dto.BusinessGrade;
import mutsa.backend.BusinessDistrict.dto.ppl.*;
import mutsa.backend.BusinessDistrict.dto.sales.BusinessRankResponse;
import mutsa.backend.BusinessDistrict.dto.shop.CategoryCountResponse;
import mutsa.backend.BusinessDistrict.dto.shop.RatioPieResponse;
import mutsa.backend.BusinessDistrict.dto.shop.TypeResponse;
import mutsa.backend.BusinessDistrict.repository.BusinessPPlRepository;
import mutsa.backend.BusinessDistrict.service.BusinessService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/business")
@SecurityRequirement(name = "BearerAuth")
public class BusinessController {
    private final BusinessService businessService;
    private final BusinessPPlRepository repo;

    // 1) 특정 동(dong)의 업종 분포
    @GetMapping("/districts/{dong}")
    public ResponseEntity<List<CategoryCountResponse>> distribution(@PathVariable String dong) {
        return ResponseEntity.ok(businessService.distributionByDong(dong));
    }

    // 2) 특정 동(dong)의 개업/폐업률 (백분위)
    @GetMapping("/districts/{dong}/ratio")
    public ResponseEntity<?> ratioPie(
            @PathVariable String dong,
            @RequestParam(name = "withNeighbors", defaultValue = "false") boolean withNeighbors,
            @RequestParam(name = "k", defaultValue = "2") int k
    ) {
        if (!withNeighbors) {
            return ResponseEntity.ok(businessService.ratioPieByDong(dong));
        }
        // 기준 동 + 인근 k개 동의 RatioPieResponse 맵 반환
        return ResponseEntity.ok(businessService.ratioWithNeighborsMap(dong, k));
    }

    // 3. 상권 유형 (표준편차 기반)
    @GetMapping("/districts/{dong}/type")
    public ResponseEntity<TypeResponse> marketType(@PathVariable String dong) {
        return ResponseEntity.ok(businessService.TypeByDong(dong));
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
    @GetMapping("/grade")
    public BusinessGrade getGrade (@RequestParam String dong){
        return businessService.getGrade(dong);
    }
}