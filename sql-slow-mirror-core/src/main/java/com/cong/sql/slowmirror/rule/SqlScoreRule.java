package com.cong.sql.slowmirror.rule;

import lombok.Data;

/**
 * SQL 评分规则
 *
 * @author cong
 * @date 2024/04/15
 */
@Data
public class SqlScoreRule {

    /**
     * 检查字段
     */
    private MatchColumn matchColumn;

    /**
     * 匹配值
     */
    private String matchValue;

    /**
     * 匹配规则
     */
    private MatchType matchType;

    /**
     * 减分值
     */
    private Integer scoreDeduction;

    /**
     * 原因
     */
    private String reason;

    /**
     * 建议
     */
    private String suggestion ;


    /**
     * 是否严格规则，是的-直接触发警告，否-依赖综合评分进行警告
     */
    private Boolean strict ;


}
