package mutsa.backend.Visa.repository;

import mutsa.backend.Visa.entity.VisaPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisaPlaceRepository extends JpaRepository<VisaPlace, Long> {


}
