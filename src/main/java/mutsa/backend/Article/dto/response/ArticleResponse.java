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
    private String authorNationality;
    private String category;
    private String businessType;
    private String name;
    private String content;
    private Long view;
    private Long like;
    private Long commentCount;
    private LocalDateTime createdAt;

    public static ArticleResponse from(Article a, long commentCount) {
        return ArticleResponse.builder()
                .articleId(a.getArticleId())
                .userId(a.getUser().getUserId())
                .authorLoginId(a.getUser().getLoginId())
                .authorNationality(a.getUser().getNationality())
                .name(a.getName())
                .content(a.getContent())
                .view(a.getView())
                .like(a.getLikeCount())
                .commentCount(commentCount)
                .category(a.getCategory())
                .businessType(a.getBusinessType())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
