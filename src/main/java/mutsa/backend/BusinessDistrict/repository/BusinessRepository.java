package mutsa.backend.BusinessDistrict.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mutsa.backend.BusinessDistrict.entity.BusinessDistrict;

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
}