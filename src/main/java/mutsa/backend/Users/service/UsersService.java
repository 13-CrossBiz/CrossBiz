package mutsa.backend.Users.service;
import lombok.RequiredArgsConstructor;
import mutsa.backend.Users.dto.request.UserProfileUpdateRequest;
import mutsa.backend.Users.dto.request.UserSignupBasicRequest;
import mutsa.backend.Users.dto.request.UserSignupDetailRequest;
import mutsa.backend.Users.entity.Users;
import mutsa.backend.Users.repository.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Users signupBasic(UserSignupBasicRequest req) {
        if (usersRepository.existsByLoginId(req.getLoginId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        Users user = Users.builder()
                .loginId(req.getLoginId())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .name(req.getName())
                .age(req.getAge())
                .nationality(req.getNationality())
                .status(req.getStatus())
                .build();

        return usersRepository.save(user);
    }

    @Transactional
    public Users updateDetail(Long userId, UserSignupDetailRequest req) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        if (req.getBizCategory() != null)       user.setBizCategory(req.getBizCategory());
        if (req.getEstimatePeriod() != null)    user.setEstimatePeriod(req.getEstimatePeriod());
        if (req.getWorkExperience() != null)    user.setWorkExperience(req.getWorkExperience());
        if (req.getDegree() != null)            user.setDegree(req.getDegree());
        if (req.getKoreanLevel() != null)       user.setKoreanLevel(req.getKoreanLevel());

        return user; // dirty checking으로 업데이트 반영
    }

    @Transactional
    public Users updateProfile(Long userId, UserProfileUpdateRequest req) {
        Users u = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 9개 필드만 갱신
        if (req.getName() != null)            u.setName(req.getName());
        if (req.getAge() != null)             u.setAge(req.getAge());
        if (req.getNationality() != null)     u.setNationality(req.getNationality());
        if (req.getStatus() != null)          u.setStatus(req.getStatus());

        if (req.getBizCategory() != null)     u.setBizCategory(req.getBizCategory());
        if (req.getEstimatePeriod() != null)  u.setEstimatePeriod(req.getEstimatePeriod());
        if (req.getWorkExperience() != null)  u.setWorkExperience(req.getWorkExperience());
        if (req.getDegree() != null)          u.setDegree(req.getDegree());
        if (req.getKoreanLevel() != null)     u.setKoreanLevel(req.getKoreanLevel());

        return u; // flush 시점에 반영됨
    }

    public Users getById(Long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }
}
