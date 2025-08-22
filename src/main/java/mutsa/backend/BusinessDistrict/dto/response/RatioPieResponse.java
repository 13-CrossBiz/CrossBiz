package mutsa.backend.BusinessDistrict.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RatioPieResponse {
    private double openSharePct;   // 0 ~ 100
    private double closeSharePct;  // 0 ~ 100
}