package mutsa.backend.Article.dto.response;

import lombok.Builder;
import lombok.Getter;
import mutsa.backend.Article.entity.Comment;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {
    private Long commentId;
    private Long parentCommentId;
    private Long articleId;
    private Long userId;
    private String authorLoginId;
    private String content;
    private LocalDateTime createdAt;

    public static CommentResponse from(Comment c) {
        return CommentResponse.builder()
                .commentId(c.getCommentId())
                .parentCommentId(c.getParent() == null ? null : c.getParent().getCommentId())
                .articleId(c.getArticle().getArticleId())
                .userId(c.getUser().getUserId())
                .authorLoginId(c.getUser().getLoginId())
                .content(c.getContent())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
