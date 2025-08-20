package mutsa.backend.Users.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mutsa.backend.Users.dto.request.LoginRequest;
import mutsa.backend.Users.dto.response.LoginResponse;
import mutsa.backend.Users.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        var result = authService.login(req.getLoginId(), req.getPassword());
        return ResponseEntity.ok(new LoginResponse(result.accessToken(), result.userId(), result.loginId()));
    }
}
