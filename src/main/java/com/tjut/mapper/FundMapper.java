package com.tjut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tjut.pojo.Fund;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FundMapper extends BaseMapper<Fund> {
    List<Fund> getRecentFundList();
}
