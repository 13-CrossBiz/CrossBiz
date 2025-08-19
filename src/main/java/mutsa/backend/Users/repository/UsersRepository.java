package mutsa.backend.Users.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import mutsa.backend.Users.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
}
