package mutsa.backend.BusinessDistrict.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter @AllArgsConstructor @Builder
public class CategoryCountResponse {
    private String category; // 업종
    private Integer count; // 업종수
}