package com.tjut.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

@TableName("tb_fund")
@Data
public class Fund implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "fund_code")
    private String fundCode;
    @TableField(value = "fund_short_name")
    private String fundShortName;
    @TableField(value = "end_date")
    private Date endDate;
    @TableField(value = "unit_net_val")
    private BigDecimal unitNetVal;
}
