package mutsa.backend.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.backend.Users.dto.UserDTO;
import mutsa.backend.Users.service.CustomUserDetailsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String bearer = request.getHeader("Authorization");
        String token = (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;

        if (StringUtils.hasText(token) && jwtTokenProvider.validate(token)) {
            String loginId = jwtTokenProvider.getLoginId(token);
            UserDetails ud = userDetailsService.loadUserByUsername(loginId);

            // userId를 DTO에 넣고 SecurityContext에 저장
            if (ud instanceof UserDTO dto) {
                SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(dto));
            } else {
                SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(
                        UserDTO.builder()
                                .username(ud.getUsername())
                                .password(ud.getPassword())
                                .role(ud.getAuthorities().iterator().next().getAuthority())
                                .build()
                ));
            }
        }

        chain.doFilter(request, response);
    }
}
