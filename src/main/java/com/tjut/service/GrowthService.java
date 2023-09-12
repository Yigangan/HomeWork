package com.tjut.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tjut.dto.FundGrowthDTO;
import com.tjut.pojo.Growth;

public interface GrowthService extends IService<Growth> {


    /*
    分页
     */
    Page<FundGrowthDTO> getFundGrowthPage(int pageNum, int pageSize, int sortField, int sortDirection);

}
