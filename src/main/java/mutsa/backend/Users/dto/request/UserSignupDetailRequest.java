package mutsa.backend.Users.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 부분 업데이트를 위해 전부 nullable 허용
@Getter
@NoArgsConstructor
public class UserSignupDetailRequest {
    private String bizCategory;     // 체류자격
    private String period;          // 예상 체류 기간
    private Integer workExperience; // 근무 경력(년수)
    private String degree;          // 학위
    private String koreanLevel;     // 한국어 능력
}
