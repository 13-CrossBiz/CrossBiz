package mutsa.backend.BusinessDistrict.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "business_district",
        indexes = {
                @Index(name = "idx_business_code", columnList = "code"),
                @Index(name = "idx_business_code_category", columnList = "code, category")
        })
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class BusinessDistrict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long districtId;

    // 행정동 코드 (API 조회 키)
    @Column(nullable = false, length = 20)
    private String code;

    // 동 이름 (사람이 읽는 용)
    @Column(nullable = false, length = 50)
    private String dong;

    // 업종
    @Column(nullable = false, length = 50)
    private String category;

    // 점포 수
    @Column(nullable = false)
    private Integer count;

    // 개업률 / 폐업률 (0.0 ~ 100.0 가정)
    @Column(nullable = false)
    private Double openRatio;

    @Column(nullable = false)
    private Double closeRatio;
}