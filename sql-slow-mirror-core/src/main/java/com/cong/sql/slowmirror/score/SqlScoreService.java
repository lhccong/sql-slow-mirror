package com.cong.sql.slowmirror.score;

import com.cong.sql.slowmirror.analysis.SqlAnalysisResultList;

/**
 * SQL 分数服务
 *
 * @author cong
 * @date 2024/04/16
 */
public interface SqlScoreService {

    SqlScoreResult score(SqlAnalysisResultList sqlAnalysisResultList);
}
