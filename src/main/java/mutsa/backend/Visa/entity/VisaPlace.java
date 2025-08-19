package mutsa.backend.Visa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisaPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long visaplaceId;
    private String name;
    private Double latitude;
    private Double longitude;
    private String address;
    private String description;        // 설명
    private String phoneNumber;        // 연락처
}