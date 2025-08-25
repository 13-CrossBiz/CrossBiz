package mutsa.backend.Article.entity;

import jakarta.persistence.*;
import lombok.*;
import mutsa.backend.Users.entity.Users;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "article")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Article {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private Users user; // 작성자 (users 1:N)

    @Column(length = 50)
    private String category;

    @Column(name = "business_type", length = 50)
    private String businessType;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 5000)
    private String content;

    @Column(nullable = false)
    @Builder.Default
    private Long view = 0L;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // DB 컬럼명은 정확히 like. 예약어 충돌 방지 위해 따옴표 인용.
    @Column(name = "\"like\"", nullable = false)
    @Builder.Default
    private Long likeCount = 0L;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = java.time.LocalDateTime.now();
        if (view == null) view = 0L;
        if (likeCount == null) likeCount = 0L;
    }

    public void increaseView() { this.view = (this.view == null ? 0L : this.view) + 1; }
    public void increaseLike() { this.likeCount = (this.likeCount == null ? 0L : this.likeCount) + 1; }
    public void decreaseLike() { this.likeCount = Math.max(0L, (this.likeCount == null ? 0L : this.likeCount) - 1); }
}

