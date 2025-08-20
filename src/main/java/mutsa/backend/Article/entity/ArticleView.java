package mutsa.backend.Article.entity;

import jakarta.persistence.*;
import lombok.*;
import mutsa.backend.Users.entity.Users;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "article_view",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_article_view_user_article", columnNames = {"user_id", "article_id"})
        }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ArticleView {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleViewId;

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