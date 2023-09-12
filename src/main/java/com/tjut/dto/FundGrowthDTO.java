package com.tjut.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
public class FundGrowthDTO {
    private String fundCode;

    private String fundShortName;

    private Date endDate;

    private BigDecimal unitNetVal;

    private Float recWeek;

    private Float recOneMonth;

    private Float recThreeMonth;

    private Float recOneYear;

    private Float recTwoYear;

    private Float recThreeYear;

    private Float recFromNow;

    private Float recSinceEstab;
}
