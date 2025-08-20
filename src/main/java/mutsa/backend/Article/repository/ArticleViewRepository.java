package mutsa.backend.Article.repository;

import mutsa.backend.Article.entity.Article;
import mutsa.backend.Article.entity.ArticleView;
import mutsa.backend.Users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleViewRepository extends JpaRepository<ArticleView, Long> {
    boolean existsByUserAndArticle(Users user, Article article);
    long deleteByArticle_ArticleId(Long articleId);
}