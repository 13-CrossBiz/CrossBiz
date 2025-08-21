package mutsa.backend.BusinessDistrict.repository;

import mutsa.backend.BusinessDistrict.entity.BusinessDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
}
