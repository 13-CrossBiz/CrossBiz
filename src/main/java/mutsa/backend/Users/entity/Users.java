package mutsa.backend.Users.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import mutsa.backend.Visa.entity.Visa;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Visa> visas = new ArrayList<>();
    @Column(nullable = false, unique = true, length = 50)
    private String loginId;
    @Column(nullable = false, length = 255)
    private String passwordHash;
    @Column(nullable = false, length = 255)
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
    private String estimatePeriod; // 예상 체류 기간

    @Column(length = 50)
    private String workExperience; // 근무 경력(년수) – null 허용

    @Column(length = 50)
    private String degree;
    @Column(length = 50)
    private String koreanLevel;
}