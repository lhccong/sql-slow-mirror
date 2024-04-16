package com.cong.sql.slowmirror.score;

import lombok.Data;

/**
 * SQL 分数结果详细信息
 *
 * @author cong
 * @date 2024/04/16
 */
@Data
public class SqlScoreResultDetail {

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
     * 是否严格规则，是的-直接触发警告，否-依赖综合评分进行警告（暂不使用）
     */
    private Boolean strict ;



}
