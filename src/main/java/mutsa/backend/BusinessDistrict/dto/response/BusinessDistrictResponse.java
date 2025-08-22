package mutsa.backend.BusinessDistrict.dto.response;

import lombok.Builder;
import lombok.Getter;
import mutsa.backend.BusinessDistrict.entity.BusinessDistrict;

@Getter @Builder
public class BusinessDistrictResponse {
    private Long districtId;
    private String code;
    private String dong;
    private String category;
    private Integer count;
    private Double openRatio;
    private Double closeRatio;

    public static BusinessDistrictResponse from(BusinessDistrict bd) {
        return BusinessDistrictResponse.builder()
                .districtId(bd.getDistrictId())
                .code(bd.getCode())
                .dong(bd.getDong())
                .category(bd.getCategory())
                .count(bd.getCount())
                .openRatio(bd.getOpenRatio())
                .closeRatio(bd.getCloseRatio())
                .build();
    }
}