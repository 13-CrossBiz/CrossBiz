package mutsa.backend.Article.dto.response;

import lombok.Builder;
import lombok.Getter;
import mutsa.backend.Article.entity.Article;

import java.time.LocalDateTime;

@Getter
@Builder
public class ArticleResponse {
    private Long articleId;
    private Long userId;
    private String authorLoginId;
    private String name;
    private String content;
    private Long view;
    private Long like;            // 필드명: like
    private LocalDateTime createdAt;

    public static ArticleResponse from(Article a) {
        return ArticleResponse.builder()
                .articleId(a.getArticleId())
                .userId(a.getUser().getUserId())
                .authorLoginId(a.getUser().getLoginId())
                .name(a.getName())
                .content(a.getContent())
                .view(a.getView())
                .like(a.getLikeCount())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
