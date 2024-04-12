package com.cong.example.config;

import com.cong.sql.slowmirror.core.SqlAnalysisAspect;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 配置
 * @author <a href="https://github.com/lhccong">...</a>
 */
@Configuration
@MapperScan("com.cong.example.mapper")
public class MyBatisPlusConfig {

    /**
     * SQL分析方面
     *
     * @return {@link SqlAnalysisAspect}
     */
    @Bean
    public SqlAnalysisAspect sqlAnalysisAspect() {
        return new SqlAnalysisAspect();
    }

}