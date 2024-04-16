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
public class SqlScoreResultOutMySQL implements SqlScoreResultOutService {

    private static final Logger logger = LoggerFactory.getLogger(SqlScoreResultOutMySQL.class);

    @Override
    public void outResult(SqlScoreResult sqlScoreResult) {
        // 待实现
        logger.info("====待实现MySQL[想使用模板方法来抽象]==={}",sqlScoreResult);
    }


}
