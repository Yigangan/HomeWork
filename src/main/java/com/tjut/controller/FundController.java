package com.tjut.controller;

import com.tjut.service.FundService;
import com.tjut.service.ImportSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class FundController {

    @Autowired
    private ImportSourceService importSourceService;

    @Autowired
    private FundService fundService;

    @PostMapping(value = "/excel")
    public String excelProTbZbzs(@RequestParam("file") MultipartFile file){
        try {
            return importSourceService.importTprkxx(file).get("state").equals(200) ?"success":"fail";
        }catch (RuntimeException e){
            throw new RuntimeException("出错了");
        }
    }

    @DeleteMapping("/delete")
    public String deleteExcel(){
        return fundService.remove(null)?"success":"fail";
    }


}
