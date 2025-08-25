package mutsa.backend.Users.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileUpdateRequest {
    // 1) 기본 4개
    private String name;
    private String age;
    private String nationality;
    private String status;

    // 2) 상세 5개
    private String bizCategory;
    private String estimatePeriod;
    private String workExperience;
    private String degree;
    private String koreanLevel;
}
