package mutsa.backend.Article.service;

import lombok.RequiredArgsConstructor;
import mutsa.backend.Article.dto.response.ArticleResponse;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                .user(user).name(req.getName()).content(req.getContent()).category(req.getCategory()).businessType(req.getBusinessType())
                .build();
        return articleRepository.save(a);
    }

    @Transactional(readOnly = true)
    public Page<ArticleResponse> searchByName(int page, int size, boolean hot, String q) {
        Sort sort = hot ? Sort.by(Sort.Direction.DESC, "likeCount")
                : Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Article> pageResult = articleRepository.searchByName(q, pageable);

        // 현재 페이지의 articleIds
        List<Long> ids = pageResult.getContent().stream()
                .map(Article::getArticleId)
                .toList();

        // 댓글 수 벌크 집계
        Map<Long, Long> countMap = commentRepository.countByArticleIds(ids).stream()
                .collect(Collectors.toMap(
                        CommentRepository.CountByArticleId::getArticleId,
                        CommentRepository.CountByArticleId::getCnt
                ));

        // DTO 매핑
        List<ArticleResponse> dtoList = pageResult.getContent().stream()
                .map(a -> ArticleResponse.from(a, countMap.getOrDefault(a.getArticleId(), 0L)))
                .toList();

        return new PageImpl<>(dtoList, pageable, pageResult.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<ArticleResponse> listByAuthor(Long userId, int page, int size, boolean hot) {
        Sort sort = hot ? Sort.by(Sort.Direction.DESC, "likeCount")
                : Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Article> pageResult = articleRepository.findByUser_UserId(userId, pageable);

        // 현재 페이지의 articleIds
        List<Long> ids = pageResult.getContent().stream()
                .map(Article::getArticleId)
                .toList();

        // 댓글 수 벌크 집계
        Map<Long, Long> countMap = commentRepository.countByArticleIds(ids).stream()
                .collect(Collectors.toMap(
                        CommentRepository.CountByArticleId::getArticleId,
                        CommentRepository.CountByArticleId::getCnt
                ));

        // DTO 매핑
        List<ArticleResponse> dtoList = pageResult.getContent().stream()
                .map(a -> ArticleResponse.from(a, countMap.getOrDefault(a.getArticleId(), 0L)))
                .toList();

        return new PageImpl<>(dtoList, pageable, pageResult.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<ArticleResponse> list(int page, int size, boolean hot) {
        Sort sort = hot ? Sort.by(Sort.Direction.DESC, "likeCount")
                : Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Article> pageResult = articleRepository.findAll(pageable); // user fetch됨

        // 현재 페이지의 articleIds
        List<Long> ids = pageResult.getContent().stream()
                .map(Article::getArticleId)
                .collect(Collectors.toList());

        // 댓글 수 벌크 집계 -> Map(articleId -> count)
        Map<Long, Long> countMap = commentRepository.countByArticleIds(ids).stream()
                .collect(Collectors.toMap(
                        CommentRepository.CountByArticleId::getArticleId,
                        CommentRepository.CountByArticleId::getCnt
                ));

        // DTO 매핑
        List<ArticleResponse> dtoList = pageResult.getContent().stream()
                .map(a -> ArticleResponse.from(a, countMap.getOrDefault(a.getArticleId(), 0L)))
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, pageResult.getTotalElements());
    }

    /** 로그인 유저면 최초 1회만 view +1 하면서 단건 DTO 반환 */
    @Transactional
    public ArticleResponse getAndIncreaseViewIfFirst(Long id, Long viewerUserId) {
        Article a = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (viewerUserId != null) {
            articleViewService.markFirstViewAndIncrease(viewerUserId, id);
        }
        long commentCount = commentRepository.countByArticle_ArticleId(id); // 대댓글 포함 전체 수
        return ArticleResponse.from(a, commentCount);
    }

    @Transactional
    public Article update(Long id, Long userId, ArticleUpdateRequest req) {
        Article a = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (!a.getUser().getUserId().equals(userId))
            throw new IllegalStateException("수정 권한이 없습니다.");
        if (req.getName() != null) a.setName(req.getName());
        if (req.getContent() != null) a.setContent(req.getContent());
        if (req.getCategory() != null) a.setCategory(req.getCategory());
        if (req.getBusinessType() != null) a.setBusinessType(req.getBusinessType());
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