package mutsa.backend.Article.repository;

import mutsa.backend.Article.entity.Article;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAll(Pageable pageable);
}
