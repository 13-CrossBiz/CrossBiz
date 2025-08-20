package mutsa.backend.Article.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class CommentCreateRequest {
    private Long parentCommentId; // null이면 최상위
    @NotBlank private String content;
}
