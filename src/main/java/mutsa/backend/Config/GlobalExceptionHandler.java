package mutsa.backend.Config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 로그인 실패 시 (AuthService.login()에서 던짐)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    // 로그인 요청에서 validation 실패 시 (아이디/비밀번호 누락)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidation(MethodArgumentNotValidException e,
                                                   HttpServletRequest request) {
        if ("/api/users/auth/login".equals(request.getRequestURI())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("아이디와 비밀번호는 필수 입력값입니다.");
        }
        // 다른 요청은 그대로 400
        return ResponseEntity.badRequest().body("잘못된 요청입니다.");
    }
}
