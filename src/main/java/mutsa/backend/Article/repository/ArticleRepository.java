package mutsa.backend.Article.repository;

import mutsa.backend.Article.entity.Article;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Override
    @EntityGraph(attributePaths = {"user"})   // user를 함께 로드
    Page<Article> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"user"})   // 단건도 user 함께 로드
    Optional<Article> findById(Long id);

    @EntityGraph(attributePaths = {"user"})
    Page<Article> findByUser_UserId(Long userId, Pageable pageable);

    // q(검색어)로 제목/본문/작성자ID 검색 (대소문자 무시)
    @EntityGraph(attributePaths = {"user"})
    @Query("""
    select a
    from Article a
    where (:q is null or lower(a.name) like lower(concat('%', :q, '%')))
    """)
    Page<Article> searchByName(@Param("q") String q, Pageable pageable);
}
