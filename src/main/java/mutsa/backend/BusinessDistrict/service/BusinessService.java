package mutsa.backend.BusinessDistrict.service;

import lombok.RequiredArgsConstructor;
import mutsa.backend.BusinessDistrict.dto.ppl.*;
import mutsa.backend.BusinessDistrict.dto.sales.BusinessRankResponse;
import mutsa.backend.BusinessDistrict.entity.BusinessDistrict;
import mutsa.backend.BusinessDistrict.repository.BusinessRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessService {
    private final BusinessRepository repo;

    /**
     *매출
     */
    public BusinessRankResponse getRank(String dong, int rank) {
        if (rank < 1) {
            throw new IllegalArgumentException("rank over 1");
        }
        var row = repo.findNthByDongOrderBySalesDesc(dong, rank - 1);
        if (row == null) {
            throw new IllegalArgumentException("No data. dong=" + dong + ", rank=" + rank);
        }
        return new BusinessRankResponse(
                row.getDong(),
                rank,
                row.getCategory(),
                row.getSalesAmount()
        );
    }

    public List<BusinessRankResponse> getAll(String dong, int n) {
        List<BusinessDistrict> rows = repo.findTopNByDong(dong, n);
        List<BusinessRankResponse> list = new ArrayList<>(rows.size());
        int r = 1;
        for (BusinessDistrict row : rows) {
            list.add(new BusinessRankResponse(
                    row.getDong(),
                    r++,
                    row.getCategory(),
                    row.getSalesAmount()
            ));
        }
        return list;
    }
    /**
     *유동인구
     */
    public BusinessGender getGender(String dong) {
        BusinessDistrict district = repo.findByDong(dong)
                .orElseThrow(() -> new RuntimeException("No dong-info"));
        return new BusinessGender(
                district.getDong(),
                district.getTotalMale(),
                district.getTotalFemale()
        );
    }
    public BusinessAge getAge(String dong) {
        BusinessDistrict district = repo.findByDong(dong)
                .orElseThrow(() -> new RuntimeException("No dong-info"));
        return new BusinessAge(
                district.getDong(),
                district.getPplAge10(),
                district.getPplAge20(),
                district.getPplAge30(),
                district.getPplAge40(),
                district.getPplAge50(),
                district.getPplAge60()
        );
    }
    public BusinessTime getTime(String dong){
        BusinessDistrict district = repo.findByDong(dong)
                .orElseThrow(()-> new RuntimeException("No dong-info"));
        return new BusinessTime(
                district.getDong(),
                district.getPplTime0006(),
                district.getPplTime0611(),
                district.getPplTime1114(),
                district.getPplTime1417(),
                district.getPplTime1721(),
                district.getPplTime2124()
        );
    }
    public BusinessDay getDay(String dong){
        BusinessDistrict district = repo.findByDong(dong)
                .orElseThrow(()-> new RuntimeException("No dong-info"));
        return new BusinessDay(
                district.getDong(),
                district.getPplMonday(),
                district.getPplTuesday(),
                district.getPplWednesday(),
                district.getPplThursday(),
                district.getPplFriday(),
                district.getPplSaturday(),
                district.getPplSunday()
        );
    }
    public List<BusinessTopn> getTopn() {
        return repo.findAllOrderByTotalPplDesc();
    }
}
