package mutsa.backend.Visa.dto.request.visa;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithVisaInfo {

    private String stayPeriod;
    private String visaType;
    private LocalDate issuedDate;
    private LocalDate expiryDate;
    private String businessRegNumber;
    private Long annualRevenue;
    private Integer employeeCount;
}
