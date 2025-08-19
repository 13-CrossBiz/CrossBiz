package mutsa.backend.Users.entity;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;
import mutsa.backend.Visa.entity.Visa;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "visa_id")
    private Visa visa;
    private String passwordHash;
    private String name;
    private Long age;
    private String bizStatus;
    private String nationality;
    private String status;          // TODO: 체류 자격 (enum 전환)
    private String bizCategory;     // (enum 전환)
    private Long estimatePeriod;            // 예상 체류기간
    private Long workExperience;
    private String degree;          // TODO: 학력 (enum 전환)
    private String koreanLevel;     // TOPIK 등급/기타
}
