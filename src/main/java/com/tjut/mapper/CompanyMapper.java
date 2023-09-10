package com.tjut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tjut.pojo.Company;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyMapper extends BaseMapper<Company> {
}
