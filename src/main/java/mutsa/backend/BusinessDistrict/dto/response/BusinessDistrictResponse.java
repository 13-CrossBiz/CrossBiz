package mutsa.backend.BusinessDistrict.dto.response;

import lombok.Builder;
import lombok.Getter;
import mutsa.backend.BusinessDistrict.entity.BusinessShop;

@Getter @Builder
public class BusinessDistrictResponse {
    private Long districtId;
    private String code;
    private String dong;
    private String category;
    private Integer count;
    private Double openRatio;
    private Double closeRatio;

    public static BusinessDistrictResponse from(BusinessShop bd) {
        return BusinessDistrictResponse.builder()
                .districtId(bd.getBusinshopId())
                .code(bd.getCode())
                .dong(bd.getDong())
                .category(bd.getCategory())
                .count(bd.getCount())
                .openRatio(bd.getOpenRatio())
                .closeRatio(bd.getCloseRatio())
                .build();
    }
}