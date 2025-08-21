package mutsa.backend.BusinessDistrict.service;

import lombok.RequiredArgsConstructor;
import mutsa.backend.BusinessDistrict.dto.BusinessRankResponse;
import mutsa.backend.BusinessDistrict.entity.BusinessDistrict;
import mutsa.backend.BusinessDistrict.repository.BusinessRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessService {
    private final BusinessRepository repo;

    // n위
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

}
