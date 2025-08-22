package mutsa.backend.BusinessDistrict.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import mutsa.backend.BusinessDistrict.entity.BusinessDistrict;
import mutsa.backend.BusinessDistrict.dto.ppl.BusinessTopn;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface BusinessRepository extends JpaRepository<BusinessDistrict, Long> {

    // 동 이름으로 전체 레코드 조회
    List<BusinessDistrict> findAllByDong(String dong);

    // 동 이름으로 업종 분포 집계
    @Query("""
        select bd.category as category, sum(bd.count) as totalCount
        from BusinessDistrict bd
        where bd.dong = :dong
        group by bd.category
    """)
    List<Object[]> sumCountGroupByCategory(String dong);

    boolean existsByDong(String dong);
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
