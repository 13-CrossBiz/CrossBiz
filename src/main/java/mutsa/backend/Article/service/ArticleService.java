package mutsa.backend.Article.service;

import lombok.RequiredArgsConstructor;
import mutsa.backend.Article.repository.ArticleLikeRepository;
import mutsa.backend.Article.repository.ArticleViewRepository;
import mutsa.backend.Article.repository.CommentRepository;
import mutsa.backend.Users.entity.Users;
import mutsa.backend.Users.repository.UsersRepository;
import mutsa.backend.Article.dto.request.*;
import mutsa.backend.Article.entity.Article;
import mutsa.backend.Article.repository.ArticleRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UsersRepository usersRepository;
    private final ArticleViewService articleViewService;
    private final ArticleLikeService articleLikeService;
    private final CommentRepository commentRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleViewRepository articleViewRepository;

    @Transactional
    public Article create(Long userId, ArticleCreateRequest req) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Article a = Article.builder()
                .user(user).name(req.getName()).content(req.getContent())
                .build();
        return articleRepository.save(a);
    }

    @Transactional(readOnly = true)
    public Page<Article> list(int page, int size, boolean hot) {
        Sort sort = hot ? Sort.by(Sort.Direction.DESC, "likeCount")
                : Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        return articleRepository.findAll(pageable);
    }

    /** 로그인 유저면 최초 1회만 view +1 */
    @Transactional
    public Article getAndIncreaseViewIfFirst(Long id, Long viewerUserId) {
        Article a = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (viewerUserId != null) {
            articleViewService.markFirstViewAndIncrease(viewerUserId, id);
        }
        return a;
    }

    @Transactional
    public Article update(Long id, Long userId, ArticleUpdateRequest req) {
        Article a = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (!a.getUser().getUserId().equals(userId))
            throw new IllegalStateException("수정 권한이 없습니다.");
        if (req.getName() != null) a.setName(req.getName());
        if (req.getContent() != null) a.setContent(req.getContent());
        return a;
    }

    @Transactional
    public void delete(Long articleId, Long requesterUserId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!article.getUser().getUserId().equals(requesterUserId)) {
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }

        // 1) 댓글
        commentRepository.deleteByArticle_ArticleId(articleId);

        // 2) 좋아요
        articleLikeRepository.deleteByArticle_ArticleId(articleId);

        // 3) 조회수
        articleViewRepository.deleteByArticle_ArticleId(articleId);

        // 4) 부모 삭제
        articleRepository.delete(article);
    }

    /** 유저당 1회 좋아요/취소 */
    @Transactional
    public Article like(Long id, Long userId) {
        return articleLikeService.like(userId, id);
    }

    @Transactional
    public Article unlike(Long id, Long userId) {
        return articleLikeService.unlike(userId, id);
    }
}