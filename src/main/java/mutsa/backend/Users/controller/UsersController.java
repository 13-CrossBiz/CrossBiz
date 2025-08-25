package mutsa.backend.Users.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mutsa.backend.Users.dto.UserDTO;
import mutsa.backend.Users.dto.request.UserProfileUpdateRequest;
import mutsa.backend.Users.dto.request.UserSignupBasicRequest;
import mutsa.backend.Users.dto.request.UserSignupDetailRequest;
import mutsa.backend.Users.dto.response.UserResponse;
import mutsa.backend.Users.entity.Users;
import mutsa.backend.Users.service.AuthService;
import mutsa.backend.Users.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;
    private final AuthService authService;

    // 기본가입 (공개)
    @PostMapping("/signup/basic")
    public ResponseEntity<UserResponse> signupBasic(@Valid @RequestBody UserSignupBasicRequest req) {
        Users saved = usersService.signupBasic(req);

        var result = authService.login(req.getLoginId(), req.getPassword());

        return ResponseEntity.ok(UserResponse.from(saved, result.accessToken()));
    }

    // 상세정보 (로그인 사용자 기준)
    @PatchMapping("/me/signup/detail")
    public ResponseEntity<UserResponse> signupDetail(
            @AuthenticationPrincipal UserDTO principal,
            @RequestBody UserSignupDetailRequest req
    ) {
        Users updated = usersService.updateDetail(principal.getUserId(), req);
        return ResponseEntity.ok(UserResponse.from(updated));
    }

    // 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal UserDTO principal) {
        return ResponseEntity.ok(UserResponse.from(usersService.getById(principal.getUserId())));
    }

    // 회원정보 수정
    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateMe(
            @AuthenticationPrincipal UserDTO principal,
            @RequestBody UserProfileUpdateRequest req
    ) {
        Users updated = usersService.updateProfile(principal.getUserId(), req);
        return ResponseEntity.ok(UserResponse.from(updated)); // accessToken은 null
    }
}
