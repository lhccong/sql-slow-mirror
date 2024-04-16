package com.cong.sql.slowmirror.score;

import com.cong.sql.slowmirror.analysis.SqlAnalysisResult;
import com.cong.sql.slowmirror.analysis.SqlAnalysisResultList;
import com.cong.sql.slowmirror.rule.RulesEngineExecutor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL 分数服务规则引擎
 *
 * @author cong
 * @date 2024/04/16
 */
public class SqlScoreServiceRulesEngine implements SqlScoreService {

    private static final Logger logger = LoggerFactory.getLogger(SqlScoreServiceRulesEngine.class);

    private static final Integer WARN_SCORE = 80;

    @Override
    public SqlScoreResult score(SqlAnalysisResultList sqlAnalysisResultList) {
        List<SqlAnalysisResult> resultList = sqlAnalysisResultList.getResultList();
        if (CollectionUtils.isEmpty(resultList)) {
            return null;
        }

        //默认100分，扣分制
        int score = 100;
        SqlScoreResult scoreResult = new SqlScoreResult();

        List<SqlScoreResultDetail> analysisResults = new ArrayList<>();
        //遍历分析结果，匹配评分规则
        for (SqlAnalysisResult result : resultList) {
            List<SqlScoreResultDetail> detail = matchRuleEngine(result);
            if (CollectionUtils.isNotEmpty(detail)) {
                analysisResults.addAll(detail);
            }
        }

        //计算综合评分
        for (SqlScoreResultDetail detail : analysisResults) {
            score = score - detail.getScoreDeduction();
            if (score < 0) {
                //防止出现负分
                score = 0;
                //结束循环
                break;
            }
        }

        //
        scoreResult.setNeedWarn(score < WARN_SCORE);
        //设置分数
        scoreResult.setScore(score);
        //设置分析结果
        scoreResult.setAnalysisResults(analysisResults);

        logger.info("SQL 评分结果 = {}", scoreResult);

        return scoreResult;
    }

    private List<SqlScoreResultDetail> matchRuleEngine(SqlAnalysisResult result) {
        logger.info("====开始规则引擎匹配====");

        return RulesEngineExecutor.execute(result);
    }
}
