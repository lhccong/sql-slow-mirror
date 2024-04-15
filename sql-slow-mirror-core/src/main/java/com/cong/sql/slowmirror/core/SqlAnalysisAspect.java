package com.cong.sql.slowmirror.core;

import com.cong.sql.slowmirror.analysis.SqlAnalysis;
import com.cong.sql.slowmirror.analysis.SqlAnalysisResultList;
import com.cong.sql.slowmirror.config.SqlAnalysisConfig;
import com.cong.sql.slowmirror.extract.SqlExtract;
import com.cong.sql.slowmirror.extract.SqlExtractResult;
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

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            //类型有两种 1、SQL 2、连接
            Object firstArg = invocation.getArgs()[0];
            //1、是否开启 SQL 替换 2、判断是不是SQL语句表
            if (SqlAnalysisConfig.getSqlReplaceModelSwitch() != null && SqlAnalysisConfig.getSqlReplaceModelSwitch() && firstArg instanceof MappedStatement) {
                //sql替换模块

            }
            //是否分析 SQL 且要是连接对象
            else if (SqlAnalysisConfig.isAnalysisSwitch() && firstArg instanceof Connection) {
                //sql 分析模块
                //1、获取入参statement
                StatementHandler statementHandler = (StatementHandler)invocation.getTarget();

                //2、将完整的 SQL 提取出来
                SqlExtractResult sqlExtractResult = SqlExtract.extract(statementHandler);

                if(sqlExtractResult!=null){
                    //1、对sql进行分析

                    //2、对分析结果进行评估
                }
            }
        } catch (Exception e) {
            logger.error("sql analysis error ", e);
        }
        // 放行，不改变原有流程
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        //初始化配置
        SqlAnalysisConfig.init(properties);
    }
}
