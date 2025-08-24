package mutsa.backend.BusinessDistrict.repository;

import mutsa.backend.BusinessDistrict.entity.BusinessPPl;
import mutsa.backend.BusinessDistrict.entity.BusinessSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BusinessSalesRepository extends JpaRepository <BusinessSales, Long> {

    @Query(value = """
        SELECT *
        FROM business_district
        WHERE dong = :dong
        ORDER BY sales_amount DESC
        LIMIT 1 OFFSET :offset
        """, nativeQuery = true)
    BusinessSales findNthByDongOrderBySalesDesc(
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
    List<BusinessSales> findTopNByDong(
            @Param("dong") String dong,
            @Param("limit") int limit
    );
    BusinessSales findFirstByDong(String dong);
}
