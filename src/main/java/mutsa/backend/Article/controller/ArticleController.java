package mutsa.backend.Article.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    // 생성
    @PostMapping
    public ResponseEntity<ArticleResponse> create(
            @AuthenticationPrincipal UserDTO principal,
            @Valid @RequestBody ArticleCreateRequest req) {
        Article saved = articleService.create(principal.getUserId(), req);
        return ResponseEntity.ok(ArticleResponse.from(saved));
    }

    // 목록 (hot=true면 좋아요순)
    @GetMapping
    public ResponseEntity<Page<ArticleResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean hot) {
        var result = articleService.list(page, size, hot)
                .map(ArticleResponse::from);
        return ResponseEntity.ok(result);
    }

    // 단건 조회 (로그인시 최초 1회만 조회수 +1)
    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> get(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDTO principal
    ) {
        Long viewerUserId = (principal == null ? null : principal.getUserId());
        var a = articleService.getAndIncreaseViewIfFirst(id, viewerUserId);
        return ResponseEntity.ok(ArticleResponse.from(a));
    }

    // 수정
    @PatchMapping("/{id}")
    public ResponseEntity<ArticleResponse> update(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDTO principal,
            @RequestBody ArticleUpdateRequest req) {
        var a = articleService.update(id, principal.getUserId(), req);
        return ResponseEntity.ok(ArticleResponse.from(a));
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
        return ResponseEntity.ok(ArticleResponse.from(a));
    }

    // 좋아요 취소 (좋아요 했던 유저만 감소)
    @PostMapping("/{id}/unlike")
    public ResponseEntity<ArticleResponse> unlike(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDTO principal) {
        var a = articleService.unlike(id, principal.getUserId());
        return ResponseEntity.ok(ArticleResponse.from(a));
    }
}