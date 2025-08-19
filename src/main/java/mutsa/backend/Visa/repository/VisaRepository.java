package mutsa.backend.Visa.repository;

import mutsa.backend.Visa.entity.Visa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisaRepository extends JpaRepository <Visa, Long> {
     List<Visa> findByUser_UserId(Long userId);
}
