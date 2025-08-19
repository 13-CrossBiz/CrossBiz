package mutsa.backend.Users.dto.response;

import lombok.Builder;
import lombok.Getter;
import mutsa.backend.Users.entity.Users;

@Getter
@Builder
public class UserResponse {
    private Long userId;
    private String loginId;
    private String name;
    private int age;
    private String nationality;
    private String status;
    private String bizCategory;
    private String period;
    private int workExperience;
    private String degree;
    private String koreanLevel;

    public static UserResponse from(Users u) {
        return UserResponse.builder()
                .userId(u.getUserId())
                .loginId(u.getLoginId())
                .name(u.getName())
                .age(u.getAge())
                .nationality(u.getNationality())
                .status(u.getStatus())
                .bizCategory(u.getBizCategory())
                .period(u.getPeriod())
                .workExperience( // 기본값 0
                        java.util.Optional.ofNullable(u.getWorkExperience()).orElse(0)
                )
                .degree(u.getDegree())
                .koreanLevel(u.getKoreanLevel())
                .build();
    }
}
