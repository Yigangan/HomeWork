package com.tjut.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tjut.mapper.CompanyMapper;
import com.tjut.pojo.Company;
import com.tjut.service.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanySerivceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService  {
    @Override
    public List<Company> getAllCompanyByTime() {
        return list(Wrappers.<Company>lambdaQuery().orderByDesc(Company::getOrgEstDate));
    }

    @Override
    public void addCompany(Company company) {
        save(company);
    }

}
