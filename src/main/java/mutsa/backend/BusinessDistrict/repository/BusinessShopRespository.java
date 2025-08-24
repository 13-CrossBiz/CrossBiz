package mutsa.backend.BusinessDistrict.repository;

import mutsa.backend.BusinessDistrict.entity.BusinessShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BusinessShopRespository extends JpaRepository<BusinessShop, Long> {
    // 동 이름으로 전체 레코드 조회 (가중 평균 계산에 사용)
    List<BusinessShop> findAllByDong(String dong);

    // 동 이름으로 업종 분포 집계
    @Query("""
        select bd.category as category, sum(coalesce(bd.count, 0)) as totalCount
        from BusinessShop bd
        where bd.dong = :dong
        group by bd.category
        order by totalCount desc
    """)
    List<Object[]> sumCountGroupByCategory(String dong);
}
