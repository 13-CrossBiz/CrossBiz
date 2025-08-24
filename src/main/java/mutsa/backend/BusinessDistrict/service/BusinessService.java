package mutsa.backend.BusinessDistrict.service;

import lombok.RequiredArgsConstructor;
import mutsa.backend.BusinessDistrict.dto.BusinessGrade;
import mutsa.backend.BusinessDistrict.dto.ppl.*;
import mutsa.backend.BusinessDistrict.dto.sales.BusinessRankResponse;
import mutsa.backend.BusinessDistrict.dto.shop.CategoryCountResponse;
import mutsa.backend.BusinessDistrict.dto.shop.RatioPieResponse;
import mutsa.backend.BusinessDistrict.dto.shop.TypeResponse;
import mutsa.backend.BusinessDistrict.entity.BusinessPPl;
import mutsa.backend.BusinessDistrict.entity.BusinessSales;
import mutsa.backend.BusinessDistrict.entity.BusinessShop;
import mutsa.backend.BusinessDistrict.repository.BusinessPPlRepository;
import mutsa.backend.BusinessDistrict.repository.BusinessSalesRepository;
import mutsa.backend.BusinessDistrict.repository.BusinessShopRespository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BusinessService {
    private final BusinessPPlRepository pplRepo;
    private final BusinessSalesRepository salesRepo;
    private final BusinessShopRespository shopRepo;

    /**
     *점포
     */
    // 마포구 16개 동 인접 목록 (시드용, 필요시 조정)
    private static final Map<String, List<String>> ADJ = Map.ofEntries(
            Map.entry("공덕동", List.of("아현동","도화동","염리동","용강동")),
            Map.entry("아현동", List.of("공덕동","도화동","염리동","연남동")),
            Map.entry("도화동", List.of("공덕동","용강동","대흥동","염리동")),
            Map.entry("용강동", List.of("도화동","서강동","대흥동","공덕동")),
            Map.entry("대흥동", List.of("용강동","염리동","서강동","신수동")),
            Map.entry("염리동", List.of("공덕동","대흥동","신수동","아현동")),
            Map.entry("신수동", List.of("서강동","대흥동","염리동","서교동")),
            Map.entry("서강동", List.of("합정동","신수동","용강동","서교동")),
            Map.entry("서교동", List.of("합정동","연남동","망원1동","서강동")), // 서교동 기준 예시
            Map.entry("합정동", List.of("서교동","망원1동","망원2동","서강동")),
            Map.entry("망원1동", List.of("합정동","망원2동","서교동","성산1동")),
            Map.entry("망원2동", List.of("망원1동","성산1동","합정동","성산2동")),
            Map.entry("연남동", List.of("서교동","성산1동","아현동","성산2동")),
            Map.entry("성산1동", List.of("망원1동","망원2동","연남동","성산2동")),
            Map.entry("성산2동", List.of("성산1동","망원2동","상암동","연남동")),
            Map.entry("상암동", List.of("성산2동","성산1동"))
    );

    // ===== 표준편차 설정값(필요시 조정) =====
    private static final double CENTER_THRESHOLD_PCT = 50.0; // 중심형 임계값
    private static final double BALANCED_STDDEV_MAX  = 5.0;  // 균형형 표준편차 상한(%)

    @Transactional(readOnly = true)
    public List<CategoryCountResponse> distributionByDong(String dong) {
        List<Object[]> rows = shopRepo.sumCountGroupByCategory(dong);
        return rows.stream().map(r ->
                new CategoryCountResponse((String) r[0], ((Number) r[1]).intValue())
        ).collect(Collectors.toList());
    }

    /**
     * 개업/폐업률 백분위(Pie용) 계산:
     * - 각 레코드의 (count * openRatio), (count * closeRatio)를 합산한 뒤
     *   동 전체 count 합으로 나눠 가중 평균(open, close) 산출
     * - open과 close를 합이 100이 되도록 비율로 변환하여 정수로 변환
     */
    @Transactional(readOnly = true)
    public RatioPieResponse ratioPieByDong(String dong) {
        List<BusinessShop> all = shopRepo.findAllByDong(dong);
        if (all.isEmpty()) {
            return new RatioPieResponse(0, 0);
        }

        long totalCount = all.stream()
                .map(BusinessShop::getCount)
                .filter(Objects::nonNull)
                .mapToLong(Integer::longValue)
                .sum();

        if (totalCount == 0L) {
            return new RatioPieResponse(0, 0);
        }

        double openNumerator = all.stream()
                .mapToDouble(bd -> (bd.getCount() == null ? 0 : bd.getCount())
                        * (bd.getOpenRatio() == null ? 0.0 : bd.getOpenRatio()))
                .sum();

        double closeNumerator = all.stream()
                .mapToDouble(bd -> (bd.getCount() == null ? 0 : bd.getCount())
                        * (bd.getCloseRatio() == null ? 0.0 : bd.getCloseRatio()))
                .sum();

        double openAvg = openNumerator / (double) totalCount;
        double closeAvg = closeNumerator / (double) totalCount;

        double sum = openAvg + closeAvg;
        if (sum == 0.0) {
            return new RatioPieResponse(0, 0);
        }

        // 하나는 반올림, 다른 하나는 100에서 빼서 맞추기
        long openShare = Math.round((openAvg / sum) * 100.0);
        long closeShare = 100 - openShare;

        return new RatioPieResponse(openShare, closeShare);
    }

    // ===== 표준편차(σ) 기반 상권 유형 판정 (간단 응답) =====
    @Transactional(readOnly = true)
    public TypeResponse TypeByDong(String dong) {
        List<Object[]> rows = shopRepo.sumCountGroupByCategory(dong);
        if (rows.isEmpty()) {
            return new TypeResponse(dong, "데이터 없음");
        }

        long total = rows.stream()
                .map(r -> (Number) r[1])
                .mapToLong(Number::longValue)
                .sum();
        if (total == 0L) {
            return new TypeResponse(dong, "데이터 없음");
        }

        // 카테고리별 점유율(%) 계산 (정렬은 쿼리에서 보장)
        List<String> cats = new ArrayList<>();
        List<Double> shares = new ArrayList<>();
        for (Object[] r : rows) {
            String cat = (String) r[0];
            int count = ((Number) r[1]).intValue();
            double pct = (count * 100.0) / total; // double %
            cats.add(cat);
            shares.add(pct);
        }

        // 1) 중심형: 최댓값 ≥ 50%
        double topShare = shares.get(0);
        String topCategory = cats.get(0);
        if (topShare >= CENTER_THRESHOLD_PCT) {
            return new TypeResponse(dong, topCategory + " 중심 상권");
        }

        // 2) 균형형: 표준편차 ≤ 5.0
        double avg = shares.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = shares.stream()
                .mapToDouble(s -> Math.pow(s - avg, 2))
                .average()
                .orElse(0.0);
        double stddev = Math.sqrt(variance);

        if (stddev <= BALANCED_STDDEV_MAX) {
            return new TypeResponse(dong, "균형형 상권");
        }

        // 3) 그 외: 복합형
        return new TypeResponse(dong, "복합형 상권");
    }

    // 기준 동 + 인근 k개 동의 RatioPieResponse를 Map으로 반환
    @Transactional(readOnly = true)
    public Map<String, RatioPieResponse> ratioWithNeighborsMap(String dong, int k) {
        Map<String, RatioPieResponse> result = new LinkedHashMap<>();

        // 1) 기준 동
        result.put(dong, ratioPieByDong(dong));

        // 2) 인근 k개 동
        List<String> neighbors = ADJ.getOrDefault(dong, List.of());
        for (String nd : neighbors.stream().limit(k).toList()) {
            result.put(nd, ratioPieByDong(nd));
        }
        return result;
    }
    /**
     *매출
     */
    public BusinessRankResponse getRank(String dong, int rank) {
        if (rank < 1) {
            throw new IllegalArgumentException("rank over 1");
        }
        var row = salesRepo.findNthByDongOrderBySalesDesc(dong, rank - 1);
        if (row == null) {
            throw new IllegalArgumentException("No data. dong=" + dong + ", rank=" + rank);
        }
        return new BusinessRankResponse(
                row.getDong(),
                rank,
                row.getCategory(),
                row.getSalesAmount()
        );
    }

    public List<BusinessRankResponse> getAll(String dong, int n) {
        List<BusinessSales> rows = salesRepo.findTopNByDong(dong, n);
        List<BusinessRankResponse> list = new ArrayList<>(rows.size());
        int r = 1;
        for (BusinessSales row : rows) {
            list.add(new BusinessRankResponse(
                    row.getDong(),
                    r++,
                    row.getCategory(),
                    row.getSalesAmount()
            ));
        }
        return list;
    }
    /**
     *유동인구
     */
    public BusinessGender getGender(String dong) {
        BusinessPPl district = pplRepo.findFirstByDong(dong);
        return new BusinessGender(
                district.getDong(),
                district.getTotalMale(),
                district.getTotalFemale()
        );
    }
    public BusinessAge getAge(String dong) {
        BusinessPPl district = pplRepo.findFirstByDong(dong);
        return new BusinessAge(
                district.getDong(),
                district.getPplAge10(),
                district.getPplAge20(),
                district.getPplAge30(),
                district.getPplAge40(),
                district.getPplAge50(),
                district.getPplAge60()
        );
    }
    public BusinessTime getTime(String dong){
        BusinessPPl district = pplRepo.findFirstByDong(dong);
        return new BusinessTime(
                district.getDong(),
                district.getPplTime0006(),
                district.getPplTime0611(),
                district.getPplTime1114(),
                district.getPplTime1417(),
                district.getPplTime1721(),
                district.getPplTime2124()
        );
    }
    public BusinessDay getDay(String dong){
        BusinessPPl district = pplRepo.findFirstByDong(dong);
        return new BusinessDay(
                district.getDong(),
                district.getPplMonday(),
                district.getPplTuesday(),
                district.getPplWednesday(),
                district.getPplThursday(),
                district.getPplFriday(),
                district.getPplSaturday(),
                district.getPplSunday()
        );
    }
    public BusinessGrade getGrade(String dong){
        BusinessPPl districtPPl   = pplRepo.findFirstByDong(dong);
        BusinessSales districtSales = salesRepo.findFirstByDong(dong);
        List<BusinessShop> shops  = shopRepo.findAllByDong(dong);

        // 방어 코드
        if (districtPPl == null || districtSales == null || shops == null || shops.isEmpty()) {
            return new BusinessGrade(dong, 5);
        }

        // 동 전체 count 합
        long totalCount = shops.stream()
                .map(BusinessShop::getCount)
                .filter(Objects::nonNull)
                .mapToLong(Integer::longValue)
                .sum();

        double openAvgPct = 0.0;
        double closeAvgPct = 0.0;

        if (totalCount > 0) {
            double openNumerator = shops.stream()
                    .mapToDouble(s -> (s.getCount() == null ? 0 : s.getCount())
                            * (s.getOpenRatio() == null ? 0.0 : s.getOpenRatio()))
                    .sum();
            double closeNumerator = shops.stream()
                    .mapToDouble(s -> (s.getCount() == null ? 0 : s.getCount())
                            * (s.getCloseRatio() == null ? 0.0 : s.getCloseRatio()))
                    .sum();

            // 각 비율이 0~100 스케일이라고 가정
            openAvgPct  = openNumerator  / (double) totalCount;
            closeAvgPct = closeNumerator / (double) totalCount;
        }

        double closeSafety = 100.0 - closeAvgPct; // 폐업 안전률(높을수록 안전)
        double openRate    = openAvgPct;

        Double sales = districtSales.getMinmaxSales(); // 0~100 가정
        Double ppl   = districtPPl.getMinmaxPpl();     // 0~100 가정

        double score =
                0.4 * (sales == null ? 0.0 : sales) +
                        0.3 * (ppl   == null ? 0.0 : ppl)   +
                        0.2 * closeSafety +
                        0.1 * openRate;

        int grade = score >= 80 ? 1 :
                score >= 65 ? 2 :
                        score >= 50 ? 3 :
                                score >= 35 ? 4 : 5;

        return new BusinessGrade(districtPPl.getDong(), grade);
    }
    public BusinessQuarter getQuarter(String dong){
        BusinessPPl districtPPl = pplRepo.findFirstByDong(dong);
        return new BusinessQuarter(
                districtPPl.getDong(),
                districtPPl.getPpl20222(),
                districtPPl.getPpl20223(),
                districtPPl.getPpl20224(),
                districtPPl.getPpl20231(),
                districtPPl.getPpl20232(),
                districtPPl.getPpl20233(),
                districtPPl.getPpl20234(),
                districtPPl.getPpl20241(),
                districtPPl.getPpl20242(),
                districtPPl.getPpl20243(),
                districtPPl.getPpl20244(),
                districtPPl.getPpl20251()
        );
    }
}
