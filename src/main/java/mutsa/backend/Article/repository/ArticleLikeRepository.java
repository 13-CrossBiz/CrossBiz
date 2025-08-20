package mutsa.backend.Article.repository;

import mutsa.backend.Article.entity.Article;
import mutsa.backend.Article.entity.ArticleLike;
import mutsa.backend.Users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    boolean existsByUserAndArticle(Users user, Article article);
    long deleteByUserAndArticle(Users user, Article article);
    long deleteByArticle_ArticleId(Long articleId);
}