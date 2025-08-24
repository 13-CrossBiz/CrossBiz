package mutsa.backend.BusinessDistrict.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long businsalesId;
    private String dong;
    private String category;
    @Column(precision = 20, scale = 2,nullable = true)
    private Long salesAmount;
    private Double minmaxSales;
    private Integer ranks;
}
