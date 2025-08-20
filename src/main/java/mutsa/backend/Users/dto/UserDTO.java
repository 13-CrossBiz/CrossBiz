package mutsa.backend.Users.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDTO implements UserDetails {
    private Long userId;
    private String username;   // loginId
    private String password;   // passwordHash
    private String role;       // e.g. ROLE_USER

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role == null ? "ROLE_USER" : role));
    }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
