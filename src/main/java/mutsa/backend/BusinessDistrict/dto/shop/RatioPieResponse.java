package mutsa.backend.BusinessDistrict.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RatioPieResponse {
    private double openSharePct;   // 개업률 (0 ~ 100)
    private double closeSharePct;  // 폐업률 (0 ~ 100)
}