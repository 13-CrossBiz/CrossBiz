package mutsa.backend.Article.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mutsa.backend.Article.repository.CommentRepository;
import mutsa.backend.Users.dto.UserDTO;
import mutsa.backend.Article.dto.request.*;
import mutsa.backend.Article.dto.response.*;
import mutsa.backend.Article.entity.Article;
import mutsa.backend.Article.service.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final CommentRepository commentRepository;

    // 생성
    @PostMapping
    public ResponseEntity<ArticleResponse> create(
            @AuthenticationPrincipal UserDTO principal,
            @Valid @RequestBody ArticleCreateRequest req) {
        Article saved = articleService.create(principal.getUserId(), req);
        long commentCount = 0L; // 새 글은 0
        return ResponseEntity.ok(ArticleResponse.from(saved, commentCount));
    }

    // 목록 조회
    @GetMapping
    public Page<ArticleResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean hot,
            @RequestParam(required = false) String q //검색어
    ) {
        if (q != null && !q.isBlank()) {
            return articleService.searchByName(page, size, hot, q);
        }
        return articleService.list(page, size, hot);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ArticleResponse getOne(
            @PathVariable Long id,
            @AuthenticationPrincipal(expression = "userId") Long userId // 구현에 맞춰 추출
    ) {
        return articleService.getAndIncreaseViewIfFirst(id, userId);
    }

    // 내 게시물 조회
    @GetMapping("/me")
    public Page<ArticleResponse> myArticles(
            @AuthenticationPrincipal UserDTO principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean hot
    ) {
        return articleService.listByAuthor(principal.getUserId(), page, size, hot);
    }

    // 수정
    @PatchMapping("/{id}")
    public ResponseEntity<ArticleResponse> update(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDTO principal,
            @RequestBody ArticleUpdateRequest req) {
        var a = articleService.update(id, principal.getUserId(), req);
        long commentCount = commentRepository.countByArticle_ArticleId(id);
        return ResponseEntity.ok(ArticleResponse.from(a, commentCount));
    }

    // 삭제(물리)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDTO principal) {
        articleService.delete(id, principal.getUserId());
        return ResponseEntity.noContent().build();
    }

    // 좋아요 (유저당 1회)
    @PostMapping("/{id}/like")
    public ResponseEntity<ArticleResponse> like(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDTO principal) {
        var a = articleService.like(id, principal.getUserId());
        long commentCount = commentRepository.countByArticle_ArticleId(id);
        return ResponseEntity.ok(ArticleResponse.from(a, commentCount));
    }

    // 좋아요 취소 (좋아요 했던 유저만 감소)
    @PostMapping("/{id}/unlike")
    public ResponseEntity<ArticleResponse> unlike(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDTO principal) {
        var a = articleService.unlike(id, principal.getUserId());
        long commentCount = commentRepository.countByArticle_ArticleId(id);
        return ResponseEntity.ok(ArticleResponse.from(a, commentCount));
    }
}