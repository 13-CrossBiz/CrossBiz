package mutsa.backend.Article.entity;

import jakarta.persistence.*;
import lombok.*;
import mutsa.backend.Users.entity.Users;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "article_like",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_article_like_user_article", columnNames = {"user_id", "article_id"})
        }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ArticleLike {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleLikeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @PrePersist void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}