package com.tjut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tjut.dto.FundGrowthDTO;
import com.tjut.mapper.FundMapper;
import com.tjut.mapper.GrowthMapper;
import com.tjut.pojo.Fund;
import com.tjut.pojo.Growth;
import com.tjut.service.GrowthService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GrowthServiceImpl extends ServiceImpl<GrowthMapper, Growth> implements GrowthService {

    @Autowired
    private FundMapper fundMapper;

    private List<Fund> FundList;

    private List<Fund> ComputeFundList;

    @Override
    @Cacheable(value = "FundGrowthDTO",key = "#pageNum")
    public Page<FundGrowthDTO> getFundGrowthPage(int pageNum, int pageSize, int sortField, int sortDirection) {
        //getFundGrowthListCompute();
        Page<Growth> pageInfo = new Page<>(pageNum, pageSize);
        Page<FundGrowthDTO> fundGrowthDtoPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Growth> lambdaQueryWrapper = new LambdaQueryWrapper();
        //0-8 代表从单位净值和近一周到成立来的排序 0升 1降
        getSort(lambdaQueryWrapper, sortField, sortDirection);

        this.page(pageInfo, lambdaQueryWrapper);
        BeanUtils.copyProperties(pageInfo, fundGrowthDtoPage, "records");
        List<Growth> growthList = pageInfo.getRecords();
        List<FundGrowthDTO> fundGrowthDtos = growthList.stream().map(item -> {
            FundGrowthDTO fundGrowthDto = new FundGrowthDTO();
            BeanUtils.copyProperties(item, fundGrowthDto);
            initFundList(item.getFundCode(),1);
            Fund fund = getFundFromList(FundList, item);
            BeanUtils.copyProperties(fund,fundGrowthDto);
            this.FundList=null;
            return fundGrowthDto;
        }).collect(Collectors.toList());
        fundGrowthDtoPage.setRecords(fundGrowthDtos);
        return fundGrowthDtoPage;
    }


    /*
    封装DTO时查询fund
     */
    private  Fund getFundFromList(List<Fund> fundList,Growth growth) {
        for (int i = fundList.size() - 1; i >= 0; i--) {
            if(fundList.get(i).getUnitNetVal().compareTo(growth.getUnitNetVal())==0)
                return fundList.get(i);
        }
        return null;
    }

    /*
    初始化存储单个基金的数组
     */
    private void initFundList(String fundCode,int flag){
        if(flag==1) {
            this.FundList=fundMapper.selectList(Wrappers.<Fund>lambdaQuery().eq(Fund::getFundCode,fundCode));
            this.FundList=this.FundList.stream().sorted(Comparator.comparing(Fund::getEndDate)).collect(Collectors.toList());
        }else if(flag==2){
            this.ComputeFundList=fundMapper.selectList(Wrappers.<Fund>lambdaQuery().eq(Fund::getFundCode,fundCode));
            this.ComputeFundList=this.ComputeFundList.stream().sorted(Comparator.comparing(Fund::getEndDate)).collect(Collectors.toList());
        }
    }


    @Scheduled(cron = "0 0 0 * * *")
    //@Scheduled(cron="0 0/5 16,18 * * ?")  当时考虑是凌晨重新计算、当天基金会更新的时间段就多次重新计算
    public void getFundGrowthListCompute() {
        System.out.println("重新计算比率");
        List<Fund> recentFundList = fundMapper.getRecentFundList();
        recentFundList.forEach(item->{
            initFundList(item.getFundCode(),2);
            setGrowth(item);
            this.ComputeFundList=null;
        });
    }

    /*
    计算每个基金增长率
     */
    private void setGrowth(Fund fund) {
        Growth growth = new Growth();
        /*
         * 以9月8号为测试用例
         */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        try {
            c1.setTime(sdf.parse("2023-09-08"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endDate = new Date(c1.getTime().getTime());
        //正常情况
        //Date endDate=new Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        //近一周
        c.setTime(endDate);
        c.add(Calendar.DATE, -5);
        growth.setRecWeek(setOnePeriodGrowth(c, fund, false));

        //近1月 和  3月
        c.setTime(endDate);
        c.add(Calendar.MONTH, -1);
        growth.setRecOneMonth(setOnePeriodGrowth(c, fund, false));

        c.setTime(endDate);
        c.add(Calendar.MONTH, -3);
        growth.setRecThreeMonth(setOnePeriodGrowth(c, fund, false));


        //近三年
        c.setTime(endDate);
        c.add(Calendar.YEAR, -1);
        growth.setRecOneYear(setOnePeriodGrowth(c, fund, false));

        c.setTime(endDate);
        c.add(Calendar.YEAR, -2);
        growth.setRecTwoYear(setOnePeriodGrowth(c, fund, false));

        c.setTime(endDate);
        c.add(Calendar.YEAR, -3);
        growth.setRecThreeYear(setOnePeriodGrowth(c, fund, false));

        //从今年
        c.setTime(endDate);
        c.set(Calendar.DAY_OF_YEAR, c.getActualMinimum(Calendar.DAY_OF_YEAR));
        growth.setRecFromNow(setOnePeriodGrowth(c, fund, true));
        //从成立
        Fund earliest =getEarliestFromList();
        Float rate = computeGrowthRate(earliest.getUnitNetVal(), fund.getUnitNetVal());
        growth.setRecSinceEstab(rate);
        BeanUtils.copyProperties(fund, growth);
        //保存到数据库
        saveOrUpdate(growth);
    }

    /*
    根据时间设置增长率
     */
    private float setOnePeriodGrowth(Calendar c, Fund fund, Boolean sinceYear) {
        java.util.Date y = c.getTime();
        Date targetDate = new Date(y.getTime());
        Fund nearestDateFund = sinceYear ? getNearestSinceNewYear(targetDate) : getNearestDateFund(targetDate);
        if (nearestDateFund == null) {
            Fund earliest = getEarliestFromList();
            return computeGrowthRate(earliest.getUnitNetVal(), fund.getUnitNetVal());
        } else {
            //计算增长率
            Float rate = computeGrowthRate(nearestDateFund.getUnitNetVal(), fund.getUnitNetVal());
            return rate;
        }
    }

    /*
    计算增长率
     */
    private Float computeGrowthRate(BigDecimal preNum, BigDecimal sufNum) {
        //(前面的数字-后面的数字)/后面的数字*100
        BigDecimal bigDecimal = sufNum.subtract(preNum).divide(preNum, 10, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
        if (bigDecimal.compareTo(BigDecimal.ZERO) != 0) {
            return bigDecimal.floatValue();
        }
        return 0f;
    }

    /*
    拿到离目标日期最近的日期
     */
    private Fund getNearestDateFund(Date date) {
        for (int i = ComputeFundList.size() - 1; i >= 0; i--) {
            if(ComputeFundList.get(i).getEndDate().getTime()-date.getTime()<=0){
                return ComputeFundList.get(i);
            }
        }
        return null;
    }

    /*
    拿到今年开始第一条数据
     */
    private Fund getNearestSinceNewYear(Date date) {
        for (Fund fund1 : this.ComputeFundList) {
            if(fund1.getEndDate().getTime()-date.getTime()>=0)
                return fund1;
        }
        return null;
    }

    /*
    拿到最早一条数据
     */
    private Fund getEarliestFromList(){
        return ComputeFundList.get(0);
    }

    /*
    排序
     */
    private void getSort(LambdaQueryWrapper<Growth> lambdaQueryWrapper, int sortField, int sortDirection) {
        if (sortField == 0 && sortDirection == 1) {
            lambdaQueryWrapper.orderByDesc(Growth::getUnitNetVal);
        } else if (sortField == 0 && sortDirection == 0) {
            lambdaQueryWrapper.orderByAsc(Growth::getUnitNetVal);
        } else if (sortField == 1 && sortDirection == 1) {
            lambdaQueryWrapper.orderByDesc(Growth::getRecWeek);
        } else if (sortField == 1 && sortDirection == 0) {
            lambdaQueryWrapper.orderByAsc(Growth::getRecWeek);
        } else if (sortField == 2 && sortDirection == 1) {
            lambdaQueryWrapper.orderByDesc(Growth::getRecOneMonth);
        } else if (sortField == 2 && sortDirection == 0) {
            lambdaQueryWrapper.orderByAsc(Growth::getRecOneMonth);
        } else if (sortField == 3 && sortDirection == 1) {
            lambdaQueryWrapper.orderByDesc(Growth::getRecThreeMonth);
        } else if (sortField == 3 && sortDirection == 0) {
            lambdaQueryWrapper.orderByAsc(Growth::getRecThreeMonth);
        } else if (sortField == 4 && sortDirection == 1) {
            lambdaQueryWrapper.orderByDesc(Growth::getRecOneYear);
        } else if (sortField == 4 && sortDirection == 0) {
            lambdaQueryWrapper.orderByAsc(Growth::getRecOneYear);
        } else if (sortField == 5 && sortDirection == 1) {
            lambdaQueryWrapper.orderByDesc(Growth::getRecTwoYear);
        } else if (sortField == 5 && sortDirection == 0) {
            lambdaQueryWrapper.orderByAsc(Growth::getRecTwoYear);
        } else if (sortField == 6 && sortDirection == 1) {
            lambdaQueryWrapper.orderByDesc(Growth::getRecThreeYear);
        } else if (sortField == 6 && sortDirection == 0) {
            lambdaQueryWrapper.orderByAsc(Growth::getRecThreeYear);
        } else if (sortField == 7 && sortDirection == 1) {
            lambdaQueryWrapper.orderByDesc(Growth::getRecFromNow);
        } else if (sortField == 7 && sortDirection == 0) {
            lambdaQueryWrapper.orderByAsc(Growth::getRecFromNow);
        } else if (sortField == 8 && sortDirection == 1) {
            lambdaQueryWrapper.orderByDesc(Growth::getRecSinceEstab);
        } else if (sortField == 8 && sortDirection == 0) {
            lambdaQueryWrapper.orderByAsc(Growth::getRecSinceEstab);
        }
    }
}
