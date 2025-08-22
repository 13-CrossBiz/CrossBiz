package mutsa.backend.BusinessDistrict.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter @AllArgsConstructor @Builder
public class RatioResponse {
    private Double openRatio;   // 가중 평균
    private Double closeRatio;  // 가중 평균
}