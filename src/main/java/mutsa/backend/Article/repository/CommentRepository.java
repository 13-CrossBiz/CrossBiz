package mutsa.backend.Article.repository;

import mutsa.backend.Article.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleOrderByCreatedAtAsc(Article article);
    long deleteByArticle_ArticleId(Long articleId);
}
