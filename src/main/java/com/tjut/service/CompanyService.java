package com.tjut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tjut.pojo.Company;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CompanyService extends IService<Company> {

    /*
    按时间倒序获得所有企业
     */
    List<Company> getAllCompanyByTime();

    /*
    添加公司
     */
    void addCompany(Company company);
}
