package mutsa.backend.Article.service;

import lombok.RequiredArgsConstructor;
import mutsa.backend.Article.entity.Article;
import mutsa.backend.Article.entity.ArticleLike;
import mutsa.backend.Article.repository.ArticleLikeRepository;
import mutsa.backend.Article.repository.ArticleRepository;
import mutsa.backend.Users.entity.Users;
import mutsa.backend.Users.repository.UsersRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleRepository articleRepository;
    private final UsersRepository usersRepository;

    /** 이미 좋아요면 no-op, 처음이면 insert + likeCount++  */
    @Transactional
    public Article like(Long userId, Long articleId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (articleLikeRepository.existsByUserAndArticle(user, article)) {
            return article; // idempotent
        }
        try {
            articleLikeRepository.save(
                    ArticleLike.builder().user(user).article(article).build()
            );
            article.increaseLike();
            return article;
        } catch (DataIntegrityViolationException e) {
            // 유니크 충돌시(동시성) 카운트 증가 없이 그대로
            return article;
        }
    }

    /** 좋아요 취소: row가 있을 때만 delete + likeCount-- */
    @Transactional
    public Article unlike(Long userId, Long articleId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        long deleted = articleLikeRepository.deleteByUserAndArticle(user, article);
        if (deleted > 0) {
            article.decreaseLike();
        }
        return article;
    }
}