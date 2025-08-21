package mutsa.backend.BusinessDistrict.dto;

public record BusinessRankResponse(
        String dong,
        int rank,
        String category,
        Long salesAmount
) {}
