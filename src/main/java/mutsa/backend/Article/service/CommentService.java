package mutsa.backend.Article.service;

import lombok.RequiredArgsConstructor;
import mutsa.backend.Users.entity.Users;
import mutsa.backend.Users.repository.UsersRepository;
import mutsa.backend.Article.dto.request.CommentCreateRequest;
import mutsa.backend.Article.entity.*;
import mutsa.backend.Article.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public Comment create(Long userId, Long articleId, CommentCreateRequest req) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Comment parent = null;
        if (req.getParentCommentId() != null) {
            parent = commentRepository.findById(req.getParentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));

            // ★ 같은 글인지 검증 (다른 글의 댓글에 달지 못하도록)
            if (!parent.getArticle().getArticleId().equals(articleId)) {
                throw new IllegalArgumentException("부모 댓글과 게시글이 일치하지 않습니다.");
            }

            // ★ 깊이 제한: 대댓글까지만 허용 (parent의 parent가 있으면 금지)
            if (parent.getParent() != null) {
                throw new IllegalStateException("대댓글까지만 작성할 수 있습니다.");
            }
        }

        Comment c = Comment.builder()
                .user(user)
                .article(article)
                .parent(parent) // parent가 null이면 최상위, 아니면 '대댓글'
                .content(req.getContent())
                .build();

        return commentRepository.save(c);
    }

    @Transactional(readOnly = true)
    public List<Comment> listByArticle(Long articleId) {
        Article a = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return commentRepository.findByArticleOrderByCreatedAtAsc(a);
    }

    @Transactional
    public void delete(Long commentId, Long userId) {
        Comment c = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        if (!c.getUser().getUserId().equals(userId)) throw new IllegalStateException("삭제 권한이 없습니다.");
        commentRepository.delete(c); // 물리 삭제
    }
}
