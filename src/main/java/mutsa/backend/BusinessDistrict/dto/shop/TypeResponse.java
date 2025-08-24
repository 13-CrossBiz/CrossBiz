package mutsa.backend.BusinessDistrict.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TypeResponse {
    private String dong;   // 동 이름
    private String label;  // "요식업 중심 상권" | "균형형 상권" | "복합형 상권" | "데이터 없음"
}