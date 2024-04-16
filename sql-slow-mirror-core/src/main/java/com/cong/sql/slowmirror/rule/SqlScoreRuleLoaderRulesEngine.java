package com.cong.sql.slowmirror.rule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQL 分数规则加载程序规则引擎
 *
 * @author cong
 * @date 2024/04/16
 */
public class SqlScoreRuleLoaderRulesEngine implements SqlScoreRuleLoader {

    private static final Logger logger = LoggerFactory.getLogger(SqlScoreRuleLoaderRulesEngine.class);


    @Override
    public boolean loadScoreRule() {
        logger.info("===规则引擎配置初始化===");
        return RulesEngineExecutor.refresh();
    }

}
