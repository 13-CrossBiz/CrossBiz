package mutsa.backend.Users.service;

import lombok.RequiredArgsConstructor;
import mutsa.backend.Users.dto.UserDTO;
import mutsa.backend.Users.entity.Users;
import mutsa.backend.Users.repository.UsersRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Users u = usersRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return UserDTO.builder()
                .userId(u.getUserId())
                .username(u.getLoginId())
                .password(u.getPasswordHash())
                .role("ROLE_USER")
                .build();
    }
}
