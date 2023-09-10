package com.tjut.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName(value = "tb_growth")
@Data
public class Growth implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "fund_code")
    private String fundCode;

    @TableField(value = "rec_week")
    private Float recWeek;

    @TableField(value = "rec_one_month")
    private Float recOneMonth;

    @TableField(value = "rec_three_month")
    private Float recThreeMonth;

    @TableField(value = "rec_one_year")
    private Float recOneYear;

    @TableField(value = "rec_two_year")
    private Float recTwoYear;

    @TableField(value = "rec_three_year")
    private Float recThreeYear;

    @TableField(value = "rec_from_now")
    private Float recFromNow;

    @TableField(value = "rec_since_estab")
    private Float recSinceEstab;

    @TableField(value = "unit_net_val")
    private BigDecimal unitNetVal;

}
