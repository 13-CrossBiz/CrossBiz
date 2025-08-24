package mutsa.backend.BusinessDistrict.repository;
import java.util.List;
import mutsa.backend.BusinessDistrict.entity.BusinessPPl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import mutsa.backend.BusinessDistrict.dto.ppl.BusinessTopn;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface BusinessPPlRepository extends JpaRepository<BusinessPPl, Long> {


    @Query("SELECT b.dong AS dong, b.totalPpl AS totalPpl " +
            "FROM BusinessPPl b WHERE b.totalPpl = (SELECT MAX(bp.totalPpl) FROM BusinessPPl bp WHERE bp.dong = b.dong)")
    List<BusinessTopn> findTopnTotalPplPerDong();

    BusinessPPl findFirstByDong(String dong);
}
