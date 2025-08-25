package mutsa.backend.Visa.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import mutsa.backend.Users.dto.request.UserProfileUpdateRequest;
import mutsa.backend.Users.entity.Users;
import mutsa.backend.Visa.dto.request.visa.WithVisaInfo;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long visaId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;
    private String name;
    private String visaReason;
    private String warning;
    private String description;
    private LocalDateTime createdAt;
    //user
    private String expectedBusinessType;
    private Boolean hasIntellectualProperty;
    private Long businessFund;
    private Double oasisScore;
    private WithVisaInfo withVisaInfo;
}
