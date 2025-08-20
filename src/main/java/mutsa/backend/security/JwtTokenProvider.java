package mutsa.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import mutsa.backend.Users.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("$base64:V0lLMm9GQ3h6cE1Bc3l2U1dQb2R6U0dqS1h6cTZlRFRkUm9xQnZ4dVZySk1qYzZMS0hQZ0E9PQ==")
    private String secret;

    @Value("${jwt.expiration-ms:3600000}") // 1h
    private long validityInMs;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createToken(UserDTO principal) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setSubject(principal.getUsername()) // loginId
                .claim("uid", principal.getUserId())
                .claim("role", principal.getRole() == null ? "ROLE_USER" : principal.getRole())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getLoginId(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public Long getUserId(String token) {
        Object uid = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("uid");
        return (uid instanceof Integer) ? ((Integer) uid).longValue() : (Long) uid;
    }

    public Authentication getAuthentication(UserDTO principal) {
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }
}
