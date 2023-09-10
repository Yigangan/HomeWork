package com.tjut.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tb_company")
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "orgUniCode")
    private String orgUniCode;

    private String orgChiName;

    private String induSmaPar;

    private String orgDele;

    private BigDecimal regCap;

    private Date orgEstDate;
}
