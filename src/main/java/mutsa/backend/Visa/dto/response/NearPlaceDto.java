package mutsa.backend.Visa.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NearPlaceDto {
    private Long id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String phoneNumber;
    private Double distanceMeters; // 사용자로부터의 거리(미터)
}
