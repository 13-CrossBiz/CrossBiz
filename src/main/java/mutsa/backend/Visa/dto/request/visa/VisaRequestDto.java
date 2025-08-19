package mutsa.backend.Visa.dto.request.visa;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisaRequestDto {

    private BasicInfo basicInfo;
    private WithoutVisaInfo withoutVisaInfo;
    private WithVisaInfo withVisaInfo;
}
