package mutsa.backend.Visa.entity;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;

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

    private String name;
    private String visaReason;
    private String warning;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime createdAt;
}
