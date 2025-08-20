package mutsa.backend.Users.service;

import lombok.RequiredArgsConstructor;
import mutsa.backend.Users.dto.UserDTO;
import mutsa.backend.Users.entity.Users;
import mutsa.backend.Users.repository.UsersRepository;
import mutsa.backend.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResult login(String loginId, String rawPassword) {
        Users u = usersRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("아이디/비밀번호가 올바르지 않습니다."));
        if (!passwordEncoder.matches(rawPassword, u.getPasswordHash())) {
            throw new IllegalArgumentException("아이디/비밀번호가 올바르지 않습니다.");
        }
        UserDTO principal = UserDTO.builder()
                .userId(u.getUserId())
                .username(u.getLoginId())
                .password(u.getPasswordHash())
                .role("ROLE_USER")
                .build();
        String token = jwtTokenProvider.createToken(principal);
        return new LoginResult(token, u.getUserId(), u.getLoginId());
    }

    public record LoginResult(String accessToken, Long userId, String loginId) {}
}
