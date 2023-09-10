package com.tjut.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tjut.dto.FundGrowthDto;
import com.tjut.service.GrowthService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GrowthController {

    @Autowired
    private GrowthService growthService;


    @DeleteMapping("/deleteGrowth")
    public String deleteAllGrowth(){
        boolean remove = growthService.remove(null);
        return remove?"success":"fail";
    }

    /*
    请开发接口
    GET /fund
    请求参数
    pageNum 页数 必填
    pageSize  每页条数 必填
    sortField 排序字段 必填 默认按照单位净值顺序排序
    sortDirection 排序方向 升序还是降序 必填
     */
    @GetMapping("/fund")
    public Page<FundGrowthDto> getFundGrowthList(@RequestParam("pageNum") int pageNum,
                                                 @RequestParam("pageSize") int pageSize,
                                                 @RequestParam(value = "sortField",defaultValue = "0") int sortField ,
                                                 @RequestParam("sortDirection") int sortDirection){

        return growthService.getFundGrowthPage(pageNum,pageSize,sortField,sortDirection);
    }

}
