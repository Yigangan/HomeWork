package com.tjut.service;

import com.tjut.mapper.FundMapper;
import com.tjut.pojo.Fund;
import com.tjut.util.ImportExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImportSourceService {

    @Autowired
    private FundMapper fundMapper;

    @Transactional
    public Map<String,Object> importTprkxx(MultipartFile file){
        Map<String,Object> resultMap = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<Fund> tbZbzsList = new ArrayList<>();

        try {

            //验证文件类型
            if (!file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).equals(".xls")&&!file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).equals(".xlsx")){
                resultMap.put("mete", "文件类型有误！请上传Excle文件");
                throw new Exception("文件类型有误！请上传Excle文件");
            }

            //获取数据
            List<List<Object>> olist = ImportExcelUtil.getListByExcel(file.getInputStream(), file.getOriginalFilename());

            resultMap.put("导入成功",200);
            //封装数据
            for (int i = 0; i < olist.size(); i++) {
                List<Object> list = olist.get(i);
                if (list.get(0) == "" || ("序号").equals(list.get(0))) {
                    continue;
                }
                Fund fund=new Fund();
                //根据下标获取每一行的每一条数据
                if (String.valueOf(list.get(0))==null||String.valueOf(list.get(0)).equals("")) {
                    resultMap.put("mete", "基金代码");
                    throw new Exception("基金代码不能为空");
                }

                String s1 = String.valueOf(list.get(0));
                String[] split = s1.split("\\.");
                fund.setFundCode(split[0]);

                if (String.valueOf(list.get(1))==null||String.valueOf(list.get(1)).equals("")) {
                    resultMap.put("mete", "基金简称");
                    throw new Exception("基金简称不能为空");
                }
                fund.setFundShortName(String.valueOf(list.get(1)));

                if (String.valueOf(list.get(2))==null||String.valueOf(list.get(2)).equals("")) {
                    resultMap.put("mete", "统计时间");
                    throw new Exception("统计时间不能为空");
                }
                String s = list.get(2).toString();
                s=s.replace("/","-").substring(0,10);
                Date date = Date.valueOf(s);
                fund.setEndDate(new Date(date.getTime()));

                if(list.size()==4){
                    if (String.valueOf(list.get(3))==null||String.valueOf(list.get(3)).equals("")) {
                        resultMap.put("mete", "单位净值");
                        throw new Exception("导入失败,单位净值不能为空");
                    }

                    fund.setUnitNetVal(new BigDecimal(String.valueOf(list.get(3))));
                }
                tbZbzsList.add(fund);
            }
            List<Integer> collect = tbZbzsList.stream().map(item -> {
                int insert = fundMapper.insert(item);
                return insert;
            }).collect(Collectors.toList());
            if (!collect.contains(0)) {
                resultMap.put("state", 200);
            }else {
                resultMap.put("mete","文档内无数据，请重新导入");
                resultMap.put("state", 500);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }finally {
            return resultMap;
        }
    }
}
