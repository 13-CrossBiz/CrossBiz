package mutsa.backend.Article.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class ArticleCreateRequest {
    @NotBlank private String name;    // 제목 필드명: name
    @NotBlank private String content;
}
