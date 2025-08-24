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
    private String age;
    private String nationality;
    private String status;
    private String bizCategory;
    private String estimatePeriod;
    private String workExperience;
    private String degree;
    private String koreanLevel;

    private String accessToken;

    // 토근 없는 버전
    public static UserResponse from(Users u) {
        return UserResponse.builder()
                .userId(u.getUserId())
                .loginId(u.getLoginId())
                .name(u.getName())
                .age(u.getAge())
                .nationality(u.getNationality())
                .status(u.getStatus())
                .bizCategory(u.getBizCategory())
                .estimatePeriod(u.getEstimatePeriod())
                .workExperience(u.getWorkExperience())
                .degree(u.getDegree())
                .koreanLevel(u.getKoreanLevel())
                .accessToken(null)
                .build();
    }

    // 토큰 포함 버전
    public static UserResponse from(Users u, String token) {
        return UserResponse.builder()
                .userId(u.getUserId())
                .loginId(u.getLoginId())
                .name(u.getName())
                .age(u.getAge())
                .nationality(u.getNationality())
                .status(u.getStatus())
                .bizCategory(u.getBizCategory())
                .estimatePeriod(u.getEstimatePeriod())
                .workExperience(u.getWorkExperience())
                .degree(u.getDegree())
                .koreanLevel(u.getKoreanLevel())
                .accessToken(token) // 👈 토큰 포함
                .build();
    }
}
