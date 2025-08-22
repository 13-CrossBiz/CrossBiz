package mutsa.backend.BusinessDistrict.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @Builder
@AllArgsConstructor
public class BusinessSummaryResponse {
    private String code;
    private String dong;
    private Integer totalCount;                 // 해당 동 전체 점포 수
    private List<CategoryCountResponse> distribution; // 업종 분포
    private Double openRatio;                  // 동 전체 가중 평균 개업률
    private Double closeRatio;                 // 동 전체 가중 평균 폐업률
}
