package mutsa.backend.BusinessDistrict.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="business_ppl")
public class BusinessPPl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long businpplId;
    private Integer time;
    private String dong;

    private Long totalPpl;
    private Double minmaxPpl;
    private Long totalMale;
    private Long totalFemale;

    private Long ppl20222;
    private Long ppl20223;
    private Long ppl20224;
    private Long ppl20231;
    private Long ppl20232;
    private Long ppl20233;
    private Long ppl20234;
    private Long ppl20241;
    private Long ppl20242;
    private Long ppl20243;
    private Long ppl20244;
    private Long ppl20251;


    private Long pplTime0006;
    private Long pplTime0611;
    private Long pplTime1114;
    private Long pplTime1417;
    private Long pplTime1721;
    private Long pplTime2124;

    private Long pplMonday;
    private Long pplTuesday;
    private Long pplWednesday;
    private Long pplThursday;
    private Long pplFriday;
    private Long pplSaturday;
    private Long pplSunday;

    private Long pplAge10;
    private Long pplAge20;
    private Long pplAge30;
    private Long pplAge40;
    private Long pplAge50;
    private Long pplAge60;
}
