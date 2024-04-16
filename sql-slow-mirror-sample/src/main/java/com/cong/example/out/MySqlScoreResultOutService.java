package com.cong.example.out;

import com.cong.sql.slowmirror.out.SqlScoreResultOutService;
import com.cong.sql.slowmirror.score.SqlScoreResult;
import com.cong.sql.slowmirror.score.SqlScoreResultDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * SQL 分数结果输出服务默认服务
 *
 * @author cong
 * @date 2024/04/16
 */
public class MySqlScoreResultOutService implements SqlScoreResultOutService {

    private static final Logger logger = LoggerFactory.getLogger(MySqlScoreResultOutService.class);

    @Override
    public void outResult(SqlScoreResult sqlScoreResult) {
        logger.info("======================================自定义分析结果===================================");
        AtomicInteger atomicInteger = new AtomicInteger(1);
        if (sqlScoreResult == null) {
            return;
        }
        Consumer<SqlScoreResultDetail> printConsumer = item->{
            logger.info("=============({})命中规则===================",atomicInteger.getAndIncrement());
            logger.info("规则命中原因:{}",item.getReason());
            logger.info("规则命中,修改建议:{}",item.getSuggestion());
            logger.info("规则命中,{}{}分",item.getScoreDeduction() <0?"加上分数:+":"减去分数", -item.getScoreDeduction());

        };
        if (sqlScoreResult.getNeedWarn() != null) {
            logger.info("分析中的 SQL 语句 ID：{}",sqlScoreResult.getSqlId());
            if (Boolean.TRUE.equals(sqlScoreResult.getNeedWarn())){
                logger.error("SQL 分析结果的分数为:{},低于预期值请判断是否修改", sqlScoreResult.getScore());
                if (sqlScoreResult.getAnalysisResults() != null) {
                    sqlScoreResult.getAnalysisResults().forEach(printConsumer);
                }
            }else {

                logger.info("SQL 分析结果的分数为:{},分析正常", sqlScoreResult.getScore());
                logger.info("=====给出的修改建议如下=====");
                sqlScoreResult.getAnalysisResults().forEach(printConsumer);
                logger.info("=========================");
            }
        }
        logger.info("========================================结束=====================================");
    }


}
