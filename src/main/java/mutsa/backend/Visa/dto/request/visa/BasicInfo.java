package mutsa.backend.Visa.dto.request.visa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasicInfo {
    private Long userId;
    private Long age;
    private String bizStatus;
    private String nationality;
    private String status;          // TODO: 체류 자격 (enum 전환)
    private String bizCategory;     // (enum 전환)
    private Long estimatePeriod;            // 예상 체류기간
    private Long workExperience;
    private String degree;          // TODO: 학력 (enum 전환)
    private String koreanLevel;     // TOPIK 등급/기타
}
