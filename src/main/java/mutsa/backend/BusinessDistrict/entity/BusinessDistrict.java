package mutsa.backend.BusinessDistrict.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BusinessDistrict {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long districtId;

    private String category;
    private String dong;
    @Column(precision = 20, scale = 2,nullable = true)
    private Long salesAmount;

}
