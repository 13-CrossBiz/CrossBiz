package mutsa.backend.Users.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignupBasicRequest {

    @NotBlank
    private String loginId;

    @NotBlank
    @Size(min = 8, max = 64)
    private String password;

    // 필수
    @NotBlank
    private String name;

    @NotBlank
    private String age;

    @NotBlank
    private String nationality;

    @NotBlank
    private String status;
}
