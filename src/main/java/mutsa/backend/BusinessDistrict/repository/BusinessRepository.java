package mutsa.backend.BusinessDistrict.repository;

import mutsa.backend.BusinessDistrict.dto.ppl.BusinessTopn;
import mutsa.backend.BusinessDistrict.entity.BusinessDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface BusinessRepository extends JpaRepository<BusinessDistrict, Long> {
    @Query(value = """
        SELECT *
        FROM business_district
        WHERE dong = :dong
        ORDER BY sales_amount DESC
        LIMIT 1 OFFSET :offset
        """, nativeQuery = true)
    BusinessDistrict findNthByDongOrderBySalesDesc(
            @Param("dong") String dong,
            @Param("offset") int offset
    );
    @Query(value = """
        SELECT *
        FROM business_district
        WHERE dong = :dong
        ORDER BY sales_amount DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<BusinessDistrict> findTopNByDong(
            @Param("dong") String dong,
            @Param("limit") int limit
    );

    @Query("SELECT b.dong AS dong, b.totalPpl AS totalPpl " +
            "FROM BusinessDistrict b ORDER BY b.totalPpl DESC")
    List<BusinessTopn> findAllOrderByTotalPplDesc();
    Optional<BusinessDistrict> findByDong(String dong);
}
