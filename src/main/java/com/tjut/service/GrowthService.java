package com.tjut.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tjut.dto.FundGrowthDto;
import com.tjut.pojo.Growth;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface GrowthService extends IService<Growth> {

    /*
    * 环比增长最新一天列表
    * */
    void getFundGrowthListCompute();

    /*
    分页
     */
    Page<FundGrowthDto> getFundGrowthPage(int pageNum, int pageSize, int sortField, int sortDirection);




}
