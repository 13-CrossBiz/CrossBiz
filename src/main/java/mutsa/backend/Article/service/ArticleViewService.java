package mutsa.backend.Article.service;

import lombok.RequiredArgsConstructor;
import mutsa.backend.Article.entity.Article;
import mutsa.backend.Article.entity.ArticleView;
import mutsa.backend.Article.repository.ArticleRepository;
import mutsa.backend.Article.repository.ArticleViewRepository;
import mutsa.backend.Users.entity.Users;
import mutsa.backend.Users.repository.UsersRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleViewService {
    private final ArticleViewRepository articleViewRepository;
    private final ArticleRepository articleRepository;
    private final UsersRepository usersRepository;

    /** 최초 조회 시에만 article.view +1 */
    @Transactional
    public boolean markFirstViewAndIncrease(Long userId, Long articleId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (articleViewRepository.existsByUserAndArticle(user, article)) return false;

        try {
            articleViewRepository.save(
                    ArticleView.builder().user(user).article(article).build()
            );
            article.increaseView();
            return true;
        } catch (DataIntegrityViolationException e) {
            // 동시성 중복 insert 방어
            return false;
        }
    }
}