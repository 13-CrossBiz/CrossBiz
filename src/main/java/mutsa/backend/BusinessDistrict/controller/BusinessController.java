package mutsa.backend.BusinessDistrict.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.backend.BusinessDistrict.dto.BusinessPplResponse;
import mutsa.backend.BusinessDistrict.dto.BusinessRankResponse;
import mutsa.backend.BusinessDistrict.service.BusinessService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/business")
@SecurityRequirement(name = "BearerAuth")
public class BusinessController {
    private final BusinessService businessService;
    @GetMapping // 각 동의 매출 n순위 업종
    public BusinessRankResponse getRankByQuery(@RequestParam String dong, @RequestParam int rank) {
        return businessService.getRank(dong, rank);
    }
    @GetMapping("/dong/{dong}")// 각 동의 업종을 내림차순으로 limit개
    public List<BusinessRankResponse> getTopNByDong(@PathVariable String dong,
                                                    @RequestParam(defaultValue = "6") int limit) {
        return businessService.getAll(dong, limit);
    }
}
