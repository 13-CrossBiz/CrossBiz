package mutsa.backend.Article.repository;

import mutsa.backend.Article.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleOrderByCreatedAtAsc(Article article);
    long deleteByArticle_ArticleId(Long articleId);

    // 단건 카운트
    long countByArticle_ArticleId(Long articleId);

    // 목록 최적화용 벌크 카운트 (프로젝션으로 반환)
    interface CountByArticleId {
        Long getArticleId();
        Long getCnt();
    }

    @Query("""
        select c.article.articleId as articleId, count(c) as cnt
        from Comment c
        where c.article.articleId in :ids
        group by c.article.articleId
    """)
    List<CountByArticleId> countByArticleIds(@Param("ids") List<Long> ids);
}