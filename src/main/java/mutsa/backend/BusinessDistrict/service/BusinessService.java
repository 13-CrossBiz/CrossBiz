package mutsa.backend.BusinessDistrict.service;

import lombok.RequiredArgsConstructor;
import mutsa.backend.BusinessDistrict.dto.BusinessGrade;
import mutsa.backend.BusinessDistrict.dto.ppl.*;
import mutsa.backend.BusinessDistrict.dto.response.*;
import mutsa.backend.BusinessDistrict.dto.sales.BusinessRankResponse;
import mutsa.backend.BusinessDistrict.entity.BusinessDistrict;
import mutsa.backend.BusinessDistrict.repository.BusinessRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BusinessService {
    private final BusinessRepository repo;

    @Transactional(readOnly = true)
    public List<BusinessDistrictResponse> listByDong(String dong) {
        return repo.findAllByDong(dong).stream()
                .map(BusinessDistrictResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryCountResponse> distributionByDong(String dong) {
        List<Object[]> rows = repo.sumCountGroupByCategory(dong);
        return rows.stream().map(r ->
                new CategoryCountResponse((String) r[0], ((Number) r[1]).intValue())
        ).collect(Collectors.toList());
    }

    /**
     * 동 전체의 개업/폐업률은 "점포 수 가중 평균"으로 계산합니다. (서비스에서 직접 계산)
     * open = sum(count * openRatio) / sum(count)
     * close = sum(count * closeRatio) / sum(count)
     */
    @Transactional(readOnly = true)
    public RatioResponse ratiosByDong(String dong) {
        List<BusinessDistrict> all = repo.findAllByDong(dong);
        if (all.isEmpty()) return new RatioResponse(0.0, 0.0);

        long totalCount = all.stream()
                .map(BusinessDistrict::getCount)
                .filter(Objects::nonNull)
                .mapToLong(Integer::longValue)
                .sum();

        if (totalCount == 0L) return new RatioResponse(0.0, 0.0);

        double openNumerator = all.stream()
                .mapToDouble(bd -> (bd.getCount() == null ? 0 : bd.getCount()) *
                        (bd.getOpenRatio() == null ? 0.0 : bd.getOpenRatio()))
                .sum();

        double closeNumerator = all.stream()
                .mapToDouble(bd -> (bd.getCount() == null ? 0 : bd.getCount()) *
                        (bd.getCloseRatio() == null ? 0.0 : bd.getCloseRatio()))
                .sum();

        return new RatioResponse(openNumerator / (double) totalCount,
                closeNumerator / (double) totalCount);
    }

    @Transactional(readOnly = true)
    public BusinessSummaryResponse summaryByDong(String dong) {
        List<BusinessDistrict> all = repo.findAllByDong(dong);
        if (all.isEmpty()) {
            throw new IllegalArgumentException("해당 동의 데이터가 없습니다: " + dong);
        }
        int total = all.stream()
                .map(BusinessDistrict::getCount)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();

        List<CategoryCountResponse> dist = distributionByDong(dong);
        RatioResponse ratios = ratiosByDong(dong);

        return BusinessSummaryResponse.builder()
                .code(all.get(0).getCode()) // 원하면 여기서 code는 제거해도 됨
                .dong(dong)
                .totalCount(total)
                .distribution(dist)
                .openRatio(ratios.getOpenRatio())
                .closeRatio(ratios.getCloseRatio())
                .build();
    }

    // ratioPieByCode 를 쓰고 있었다면 dong 버전도 추가 (원형 그래프용 퍼센트)
    @Transactional(readOnly = true)
    public RatioPieResponse ratioPieByDong(String dong) {
        RatioResponse r = ratiosByDong(dong);
        double open = Math.max(0.0, r.getOpenRatio());
        double close = Math.max(0.0, r.getCloseRatio());
        double sum = open + close;
        if (sum == 0.0) return new RatioPieResponse(0.0, 0.0);
        double openShare = Math.round((open / sum * 100.0) * 100.0) / 100.0;
        double closeShare = Math.round((close / sum * 100.0) * 100.0) / 100.0;
        return new RatioPieResponse(openShare, closeShare);
    }

    /**
     * CSV 업로드 (선택): header 예시
     * dong,code,category,count,openRatio,closeRatio
     */
    @Transactional
    public int importCsv(MultipartFile file) {
        int inserted = 0;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String header = br.readLine(); // skip header
            if (header == null) return 0;

            String line;
            while ((line = br.readLine()) != null) {
                String[] t = parseCsv(line);
                if (t.length < 6) continue;

                String dong = t[0].trim();
                String code = t[1].trim();
                String category = t[2].trim();
                Integer count = Integer.valueOf(t[3].trim());
                Double openRatio = Double.valueOf(t[4].trim());
                Double closeRatio = Double.valueOf(t[5].trim());

                BusinessDistrict bd = BusinessDistrict.builder()
                        .dong(dong)
                        .code(code)
                        .category(category)
                        .count(count)
                        .openRatio(openRatio)
                        .closeRatio(closeRatio)
                        .build();
                repo.save(bd);
                inserted++;
            }
        } catch (Exception e) {
            throw new RuntimeException("CSV 파싱 중 오류: " + e.getMessage(), e);
        }
        return inserted;
    }

    // 매우 단순한 CSV 파서 (필드에 콤마 포함 안 된다고 가정)
    private String[] parseCsv(String line) {
        return line.split("\\s*,\\s*");
    }

    /**
     *매출
     */
    public BusinessRankResponse getRank(String dong, int rank) {
        if (rank < 1) {
            throw new IllegalArgumentException("rank over 1");
        }
        var row = repo.findNthByDongOrderBySalesDesc(dong, rank - 1);
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
        List<BusinessDistrict> rows = repo.findTopNByDong(dong, n);
        List<BusinessRankResponse> list = new ArrayList<>(rows.size());
        int r = 1;
        for (BusinessDistrict row : rows) {
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
        BusinessDistrict district = repo.findByDong(dong)
                .orElseThrow(() -> new RuntimeException("No dong-info"));
        return new BusinessGender(
                district.getDong(),
                district.getTotalMale(),
                district.getTotalFemale()
        );
    }
    public BusinessAge getAge(String dong) {
        BusinessDistrict district = repo.findByDong(dong)
                .orElseThrow(() -> new RuntimeException("No dong-info"));
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
        BusinessDistrict district = repo.findByDong(dong)
                .orElseThrow(()-> new RuntimeException("No dong-info"));
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
        BusinessDistrict district = repo.findByDong(dong)
                .orElseThrow(()-> new RuntimeException("No dong-info"));
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
        BusinessDistrict district = repo.findFirstByDongAndCloseRatioIsNotNull(dong)
                .orElseThrow(() -> new RuntimeException("No dong-info with close_ratio"));

        Double sales = district.getMinmaxSales();
        Double ppl = district.getMinmaxPpl();
        Double closeSafety = (1-district.getOpenRatio())*100; // 폐업 안전률
        Double openRate = district.getOpenRatio(); //개업률
        Double score = 0.4*sales + 0.3*ppl + 0.2*closeSafety+0.1*openRate;

        int grade = score >= 80 ? 1 :
                    score >= 65 ? 2 : score >= 50 ? 3 : score >= 35 ? 4 : 5;
        return new BusinessGrade(district.getDong(), grade);
    }

}
