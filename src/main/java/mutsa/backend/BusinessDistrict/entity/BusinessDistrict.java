package mutsa.backend.BusinessDistrict.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BusinessDistrict {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long districtId;
    //매출
    private String category;
    private String dong;
    @Column(precision = 20, scale = 2,nullable = true)
    private Long salesAmount;
    // 유동인구
    private Long totalPpl;
    private Long totalMale;
    private Long totalFemale;

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
