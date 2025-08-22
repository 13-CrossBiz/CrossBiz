package mutsa.backend.BusinessDistrict.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter @AllArgsConstructor @Builder
public class CategoryCountResponse {
    private String category;
    private Integer count;
}