package com.cong.sql.slowmirror.out;

import com.cong.sql.slowmirror.score.SqlScoreResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQL 分数结果输出服务默认服务
 *
 * @author cong
 * @date 2024/04/16
 */
public class SqlScoreResultOutServiceDefault implements SqlScoreResultOutService {

    private static final Logger logger = LoggerFactory.getLogger(SqlScoreResultOutServiceDefault.class);

    @Override
    public void outResult(SqlScoreResult sqlScoreResult) {
        if (sqlScoreResult == null) {
            return;
        }
        if (sqlScoreResult.getNeedWarn() != null && sqlScoreResult.getNeedWarn()) {
            logger.error("SQL 分析结果的分数为:{}", sqlScoreResult.getScore());
            if (sqlScoreResult.getAnalysisResults() != null) {
                sqlScoreResult.getAnalysisResults().forEach(result -> logger.error("导致 SQL 分数不达标的原因是:{},修改建议:{}", result.getReason(), result.getSuggestion()));
            }
        }
    }


}
