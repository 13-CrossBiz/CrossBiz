package mutsa.backend.BusinessDistrict.dto.sales;

public record BusinessRankResponse(
        String dong,
        int rank,
        String category,
        Long salesAmount
) {}
