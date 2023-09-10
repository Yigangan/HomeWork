package com.tjut;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan(basePackages = "com.tjut.mapper")
@SpringBootApplication
public class WorkApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkApplication.class,args);
    }
}
