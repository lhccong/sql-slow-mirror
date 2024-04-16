package com.cong.sql.slowmirror.score;

import lombok.Data;

import java.util.List;

/**
 * SQL 评分结果
 * @author cong
 * @date 2024/04/16
 */
@Data
public class SqlScoreResult {

    /**
     * sql id
     */
    private String sqlId;

    /**
     * 执行的原始sql
     */
    private String sourceSql;

    /**
     * 是否需要警告
     */
    private Boolean needWarn;

    /**
     * 综合评分
     */
    private Integer score;

    /**
     * 分析结果明细
     */
    List<SqlScoreResultDetail> analysisResults;

}
