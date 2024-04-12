package com.cong.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SQL 慢速镜像示例应用程序
 *
 * @author cong
 * @date 2024/04/12
 */
@SpringBootApplication
@MapperScan("com.cong.example.mapper")
public class SqlSlowMirrorSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SqlSlowMirrorSampleApplication.class, args);
    }

}
