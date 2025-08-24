package mutsa.backend.BusinessDistrict.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessShop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long businshopId;
    private Integer count;
    // 개업률 / 폐업률 (0.0 ~ 100.0 가정)
    private Double openRatio;
    private Double closeRatio;
    private String code;
    private String dong;
    private String category;
}
