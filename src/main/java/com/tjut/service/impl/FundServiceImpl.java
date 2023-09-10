package com.tjut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tjut.mapper.FundMapper;
import com.tjut.pojo.Fund;
import com.tjut.service.FundService;
import org.springframework.stereotype.Service;

@Service
public class FundServiceImpl extends ServiceImpl<FundMapper, Fund> implements FundService {

}
