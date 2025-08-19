package mutsa.backend.Users.repository;

import mutsa.backend.Users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
