package mutsa.backend.Users.dto.response;

import lombok.*;
@Getter @AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private Long userId;
    private String loginId;
}
