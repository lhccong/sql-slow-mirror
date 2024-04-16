package com.cong.sql.slowmirror.config;

import com.cong.sql.slowmirror.analysis.SqlAnalysisSqlTypeEnum;
import com.cong.sql.slowmirror.rule.SqlScoreRule;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * SQL 分析配置
 *
 * @author cong
 * @date 2024/04/12
 */
@Data
public class SqlAnalysisConfig {

    private SqlAnalysisConfig() {
        throw new IllegalStateException("Utility class");
    }


    private static final Logger logger = LoggerFactory.getLogger(SqlAnalysisConfig.class);

    /**
     * 分析开关，默认关闭
     */
    @Getter
    private static boolean analysisSwitch = false;

    /**
     * 一个id 只检查一次，默认开启
     */
    @Getter
    private static boolean onlyCheckOnce = true;

    /**
     * 两次检查间隔 默认 5分钟
     */
    @Getter
    private static Long checkInterval = 5 * 60 * 1000L;

    /**
     * 例外sql id，集合
     */
    @Getter
    private static List<String> exceptSqlIds = new ArrayList<>();

    /**
     * 进行分析的sql类型
     */
    @Getter
    private static List<String> sqlType = new ArrayList<>();


    /**
     * 评分规则加载类， 默认 com.cong.sql.slow mirror.rule.SqlScoreRuleLoaderDefault
     */
    private static String scoreRuleLoadClass;

    /**
     * 分析结果输出类，默认日志模式 com.cong.sql.slow mirror.out.SqlScoreResultOutServiceDefault
     */
    @Getter
    private static String outputModel;

    /**
     * 分析结果输出类，默认日志模式 com.cong.sql.slow mirror.out.SqlScoreResultOutServiceDefault
     */
    @Getter
    private static String outputClass;


    /**
     * 应用名称
     */
    private static String appName;

    /**
     * sqlReplaceModelSwitch 是否开启替换 SQL
     */
    @Getter
    private static Boolean sqlReplaceModelSwitch;


    /**
     * 分析开关 配置key
     */
    private static final String ANALYSIS_SWITCH_KEY = "analysisSwitch";

    /**
     * 同一id是否只检查一次 配置key
     */
    private static final String ONLY_CHECK_ONCE = "onlyCheckOnce";

    /**
     * 检查间隔时间 配置key
     */
    private static final String CHECK_INTERVAL = "checkInterval";

    /**
     * 例外sql id 配置key,多个需要逗号分隔
     */
    @Getter
    private static final String EXCEPT_SQL_IDS_KEY = "exceptSqlIds";

    /**
     * 分析开关 配置key ,多个需要逗号分隔
     */
    private static final String SQL_TYPE_KEY = "sqlType";

    /**
     * 规则加载类 配置key
     */
    private static final String SCORE_RULE_LOAD_KEY = "scoreRuleLoadClass";

    /**
     * 评分输出类 配置key
     */
    private static final String OUTPUT_CLASS_KEY = "outputClass";

    /**
     * 输出模式 配置key
     */
    private static final String OUTPUT_MODEL_KEY = "outputModel";

    /**
     * 应用名称
     */
    private static final String APP_NAME = "appName";

    /**
     * 评分规则列表
     */
    private static List<SqlScoreRule> ruleList = new ArrayList<>();


    /**
     * 初始化
     * 初始化配置
     *
     * @param properties 性能
     */
    public static void init(Properties properties){
        try{
            //加载 需要分析的sql类型
            if(StringUtils.isBlank(properties.getProperty(SQL_TYPE_KEY))){
                //默认 ，select 、update
                sqlType.add(SqlAnalysisSqlTypeEnum.SELECT.getValue());
                sqlType.add(SqlAnalysisSqlTypeEnum.UPDATE.getValue());
            }else{
                String[] sqlTypes = properties.getProperty(SQL_TYPE_KEY).split(",");
                CollectionUtils.addAll(sqlType,sqlTypes);
            }

            if(StringUtils.isNotBlank(properties.getProperty(ANALYSIS_SWITCH_KEY))){
                analysisSwitch = Boolean.parseBoolean(properties.getProperty(ANALYSIS_SWITCH_KEY));
            }
            if(StringUtils.isNotBlank(properties.getProperty(ONLY_CHECK_ONCE))){
                onlyCheckOnce = Boolean.parseBoolean(properties.getProperty(ONLY_CHECK_ONCE));
            }
            if(StringUtils.isNotBlank(properties.getProperty(CHECK_INTERVAL))){
                checkInterval = Long.valueOf(properties.getProperty(CHECK_INTERVAL));
            }
            if(StringUtils.isNotBlank(properties.getProperty(SCORE_RULE_LOAD_KEY))){
                scoreRuleLoadClass = properties.getProperty(SCORE_RULE_LOAD_KEY);
            }
            if(StringUtils.isNotBlank(properties.getProperty(OUTPUT_CLASS_KEY))){
                outputClass = properties.getProperty(OUTPUT_CLASS_KEY);
            }
            if(StringUtils.isNotBlank(properties.getProperty(OUTPUT_MODEL_KEY))){
                outputModel = properties.getProperty(OUTPUT_MODEL_KEY);
            }

            if(StringUtils.isNotBlank(properties.getProperty(EXCEPT_SQL_IDS_KEY))){
                String[] exceptIds = properties.getProperty(EXCEPT_SQL_IDS_KEY).split(",");
                CollectionUtils.addAll(exceptSqlIds,exceptIds);
            }

            if(StringUtils.isNotBlank(properties.getProperty(APP_NAME))){
                appName = properties.getProperty(APP_NAME);
            }else{
                appName = "default";
            }

            if(StringUtils.isNotBlank(properties.getProperty("sqlReplaceModelSwitch"))){
                sqlReplaceModelSwitch = Boolean.valueOf(properties.getProperty("sqlReplaceModelSwitch"));
            }



        }catch (Exception e){
            logger.error("sql analysis config init error",e);
        }

    }

}
