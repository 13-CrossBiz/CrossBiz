package mutsa.backend.BusinessDistrict.dto;

public record BusinessPplResponse(
        String dong,
        Long totalPPl,
        Long ppl0006,
        Long ppl0611,
        Long ppl1114,
        Long ppl1417,
        Long ppl1721,
        Long ppl2124
) {}