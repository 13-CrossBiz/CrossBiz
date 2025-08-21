package mutsa.backend.Users.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Users {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 50)
    private String loginId;

    @Column(nullable = false, length = 100)
    private String passwordHash;

    // 필수
    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    private String age;

    @Column(nullable = false, length = 50)
    private String nationality;

    @Column(nullable = false, length = 50)
    private String status;

    // 선택 (nullable)
    @Column(length = 50)
    private String bizCategory;   // 체류자격

    @Column(length = 50)
    private String period;        // 예상 체류 기간

    private Integer workExperience; // 근무 경력(년수) – null 허용

    @Column(length = 50)
    private String degree;

    @Column(length = 50)
    private String koreanLevel;
}
