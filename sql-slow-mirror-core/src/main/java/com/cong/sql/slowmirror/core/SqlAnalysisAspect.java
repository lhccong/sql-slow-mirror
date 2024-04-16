package com.cong.sql.slowmirror.core;

import com.cong.sql.slowmirror.analysis.SqlAnalysis;
import com.cong.sql.slowmirror.analysis.SqlAnalysisResultList;
import com.cong.sql.slowmirror.config.SqlAnalysisConfig;
import com.cong.sql.slowmirror.extract.SqlExtract;
import com.cong.sql.slowmirror.extract.SqlExtractResult;
import com.cong.sql.slowmirror.out.OutModelEnum;
import com.cong.sql.slowmirror.out.SqlScoreResultOutMq;
import com.cong.sql.slowmirror.out.SqlScoreResultOutService;
import com.cong.sql.slowmirror.out.SqlScoreResultOutServiceDefault;
import com.cong.sql.slowmirror.rule.SqlScoreRuleLoader;
import com.cong.sql.slowmirror.rule.SqlScoreRuleLoaderRulesEngine;
import com.cong.sql.slowmirror.score.SqlScoreResult;
import com.cong.sql.slowmirror.score.SqlScoreService;
import com.cong.sql.slowmirror.score.SqlScoreServiceRulesEngine;
import com.cong.sql.slowmirror.utils.GsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Optional;
import java.util.Properties;

/**
 * MyBatis 分析方面
 *
 * @author cong
 * @date 2024/04/12
 */
@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class}
), @Signature(
        type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
)})

public class SqlAnalysisAspect implements Interceptor {


    // 日志
    Logger logger = LoggerFactory.getLogger(SqlAnalysisAspect.class);

    /**
     * 评分规则服务
     */
    private static final SqlScoreService sqlScoreService = new SqlScoreServiceRulesEngine();

    /**
     * 评分结果输出服务
     */
    private static SqlScoreResultOutService sqlScoreResultOutService = new SqlScoreResultOutServiceDefault();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            //类型有两种 1、SQL 2、连接
            Object firstArg = invocation.getArgs()[0];
            //1、是否开启 SQL 替换 2、判断是不是SQL语句表
            if (SqlAnalysisConfig.getSqlReplaceModelSwitch() != null && SqlAnalysisConfig.getSqlReplaceModelSwitch() && firstArg instanceof MappedStatement) {
                //SQL 替换模块
                logger.info("开启 SQL 替换{}", "暂未开发 TODO");

            }
            //是否分析 SQL 且要是连接对象
            else if (SqlAnalysisConfig.isAnalysisSwitch() && firstArg instanceof Connection) {
                //SQL 分析模块
                //1、获取入参statement
                StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

                //2、将完整的 SQL 提取出来
                SqlExtractResult sqlExtractResult = SqlExtract.extract(statementHandler);

                if (sqlExtractResult != null) {
                    //1、对sql进行分析
                    SqlAnalysisResultList resultList = SqlAnalysis.analysis(sqlExtractResult, (Connection) firstArg);

                    //2、对分析结果进行评估
                    logger.info("分析结果：{}", resultList.getResultList());
                    Optional<SqlScoreResult> nullableOptional = Optional.ofNullable(sqlScoreService.score(resultList));

                    SqlScoreResult sqlScoreResult = getSqlScoreResult(nullableOptional, resultList, sqlExtractResult);
                    //输出评分结果
                    sqlScoreResultOutService.outResult(sqlScoreResult);
                }
            }
        } catch (Exception e) {
            logger.error("sql analysis error ", e);
        }
        // 放行，不改变原有流程
        return invocation.proceed();
    }

    private SqlScoreResult getSqlScoreResult(Optional<SqlScoreResult> optional, SqlAnalysisResultList resultList, SqlExtractResult sqlExtractResult) {
        SqlScoreResult sqlScoreResult = optional.orElseThrow(() -> {
            logger.error("SQL 分析评分异常 {},{}", GsonUtil.bean2Json(resultList), GsonUtil.bean2Json(sqlExtractResult));
            return new IllegalStateException("field is not present");
        });
        //设置 SQL ID
        sqlScoreResult.setSqlId(sqlExtractResult.getSqlId());
        //设置 SQL
        sqlScoreResult.setSourceSql(sqlExtractResult.getSourceSql());
        return sqlScoreResult;
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        //初始化配置
        SqlAnalysisConfig.init(properties);
        //初始化评分规则
        SqlScoreRuleLoader sqlScoreRuleLoader = new SqlScoreRuleLoaderRulesEngine();

        boolean loadScoreRuleRes = sqlScoreRuleLoader.loadScoreRule();
        if (!loadScoreRuleRes) {
            logger.error("==== SQL 分析规则引擎加载异常 =====");
        }

        // MQ 输出方式
        if (StringUtils.isNotBlank(SqlAnalysisConfig.getOutputModel()) && SqlAnalysisConfig.getOutputModel().toUpperCase().equals(OutModelEnum.MQ.getModelType())) {
            try {
                //1·配置文件初始化

                //2·服务初始化
                setSqlScoreOutService( new SqlScoreResultOutMq());

            } catch (Exception e) {
                logger.error("SQL 分析初始化 MQ 输出方式异常", e);
            }
        }

        //自定义输出方式加载
        if(StringUtils.isNotBlank(SqlAnalysisConfig.getOutputClass())){
            try {
                setSqlScoreOutService((SqlScoreResultOutService)Class.forName(SqlAnalysisConfig.getOutputClass()).getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                logger.error("SQL 分析初始化自定义输出方式异常",e);
            }
        }
    }

    private static void setSqlScoreOutService(SqlScoreResultOutService service) {
        sqlScoreResultOutService = service;
    }

}
