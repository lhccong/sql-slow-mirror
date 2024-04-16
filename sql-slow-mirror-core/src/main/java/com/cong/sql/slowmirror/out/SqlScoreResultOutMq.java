package com.cong.sql.slowmirror.out;

import com.cong.sql.slowmirror.score.SqlScoreResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQL 分数结果出我 SQL
 *
 * @author cong
 * @date 2024/04/16
 */
public class SqlScoreResultOutMq implements SqlScoreResultOutService {

    private static final Logger logger = LoggerFactory.getLogger(SqlScoreResultOutMq.class);

    @Override
    public void outResult(SqlScoreResult sqlScoreResult) {
        // 待实现
        logger.info("====待实现MQ发送==={}",sqlScoreResult);
    }


}
