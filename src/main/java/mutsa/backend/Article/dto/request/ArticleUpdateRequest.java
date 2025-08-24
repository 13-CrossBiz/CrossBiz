package mutsa.backend.Article.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class ArticleUpdateRequest {
    private String name;    // nullable
    private String content; // nullable
    private String category;
    private String businessType;
}
