package mutsa.backend.BusinessDistrict.service;

import lombok.RequiredArgsConstructor;
import mutsa.backend.BusinessDistrict.dto.response.*;
import mutsa.backend.BusinessDistrict.entity.BusinessDistrict;
import mutsa.backend.BusinessDistrict.repository.BusinessRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessRepository repository;

    @Transactional(readOnly = true)
    public List<BusinessDistrictResponse> listByDong(String dong) {
        return repository.findAllByDong(dong).stream()
                .map(BusinessDistrictResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryCountResponse> distributionByDong(String dong) {
        List<Object[]> rows = repository.sumCountGroupByCategory(dong);
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
        List<BusinessDistrict> all = repository.findAllByDong(dong);
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
        List<BusinessDistrict> all = repository.findAllByDong(dong);
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
                repository.save(bd);
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
}