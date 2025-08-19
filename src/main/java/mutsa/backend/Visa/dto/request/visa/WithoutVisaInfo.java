package mutsa.backend.Visa.dto.request.visa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithoutVisaInfo {

    private String career;
    private String expectedBusinessType;
    private Boolean hasIntellectualProperty;
    private Long businessFund;
    private Double oasisScore;
}

