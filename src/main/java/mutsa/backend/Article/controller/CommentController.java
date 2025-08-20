package mutsa.backend.Article.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mutsa.backend.Users.dto.UserDTO;
import mutsa.backend.Article.dto.request.CommentCreateRequest;
import mutsa.backend.Article.dto.response.CommentResponse;
import mutsa.backend.Article.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles/{articleId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> create(
            @PathVariable Long articleId,
            @AuthenticationPrincipal UserDTO principal,
            @Valid @RequestBody CommentCreateRequest req) {
        var c = commentService.create(principal.getUserId(), articleId, req);
        return ResponseEntity.ok(CommentResponse.from(c));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> list(@PathVariable Long articleId) {
        var list = commentService.listByArticle(articleId)
                .stream().map(CommentResponse::from).toList();
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long articleId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDTO principal) {
        commentService.delete(commentId, principal.getUserId());
        return ResponseEntity.noContent().build();
    }
}
