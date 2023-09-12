package com.tjut.controller;

import com.tjut.pojo.Company;
import com.tjut.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    // 查询出所有的热门企业 按照时间排序 最近创建的排在前面
    @GetMapping("/companies")
    public List<Company> getAllCompanyOrderByTime(){
        return companyService.getAllCompanyByTime();
    }

    // 新增企业
    @PostMapping("/companies")
    public void addCompany(@RequestBody Company company){
        companyService.addCompany(company);
    }

    // 查询指定企业
    @GetMapping("/companies/{comId}")
    public Company getCompanyById(@PathVariable("comId") String comId){
        return companyService.getById(comId);
    }

    // 更新指定企业
    @PutMapping("/companies/{comId}")
    public String updateCompanyById(@PathVariable("comId") String comId,@RequestBody Company company){
        return companyService.updateById(company)?"success":"fail";
    }

    // 删除指定企业
    @DeleteMapping("/companies/{comId}")
    public String deleteCompanyById(@PathVariable("comId") String comId){
        return companyService.removeById(companyService.getById(comId))?"success":"fail";
    }

}
