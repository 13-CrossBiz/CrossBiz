package mutsa.backend.BusinessDistrict.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import mutsa.backend.BusinessDistrict.dto.ppl.*;
import mutsa.backend.BusinessDistrict.dto.sales.BusinessRankResponse;
import mutsa.backend.BusinessDistrict.service.BusinessService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/business")
@SecurityRequirement(name = "BearerAuth")
public class BusinessController {
    private final BusinessService businessService;

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
        return businessService.getTopn();
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
}
