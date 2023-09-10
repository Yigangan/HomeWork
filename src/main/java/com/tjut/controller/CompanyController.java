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

    //GET /companies 查询出所有的热门企业 按照时间排序 最近创建的排在前面
    @GetMapping("/companies")
    public List<Company> getAllCompanyOrderByTime(){
        return companyService.getAllCompanyByTime();
    }

    //POST /companies 新增企业
    @PostMapping("/companies")
    public void addCompany(@RequestBody Company company){
        companyService.addCompany(company);
    }

    //GET /companies/{comId} 查询指定企业
    @GetMapping("/companies/{comId}")
    public Company getCompanyById(@PathVariable("comId") String comId){
        return companyService.getById(comId);
    }

    //PUT /companies/{comId} 更新指定企业
    @PutMapping("/companies/{comId}")
    public void updateCompanyById(@PathVariable("comId") String comId,@RequestBody Company company){
        companyService.updateById(company);
    }

    //DELETE /companies/{comId} 删除指定企业
    @DeleteMapping("/companies/{comId}")
    public void deleteCompanyById(@PathVariable("comId") String comId){
        companyService.removeById(companyService.getById(comId));
    }

}
