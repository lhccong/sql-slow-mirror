package com.cong.sql.slowmirror.rule;

/**
 * SQL 分数规则加载程序
 *
 * @author cong
 * @date 2024/04/16
 */
public interface SqlScoreRuleLoader {

    /**
     * 加载评分规则
     */
    boolean loadScoreRule();

}
