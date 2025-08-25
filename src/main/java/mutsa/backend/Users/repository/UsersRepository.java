package mutsa.backend.Users.repository;
import mutsa.backend.Users.entity.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
    Users findByName(String name);
}
