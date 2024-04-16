package com.cong.sql.slowmirror.rule;

import com.cong.sql.slowmirror.analysis.SqlAnalysisResult;
import com.cong.sql.slowmirror.score.SqlScoreResultDetail;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 规则引擎执行程序
 *
 * @author cong
 * @date 2024/04/16
 */
public class RulesEngineExecutor {

    private static final Logger logger = LoggerFactory.getLogger(RulesEngineExecutor.class);

    public static final RulesEngine RULES_ENGINE = new DefaultRulesEngine();

    private static Rules rules;

    /**
     * 匹配规则-评分结果映射关系
     */
    private static ConcurrentHashMap<String, SqlScoreResultDetail> scoreMap;

    private RulesEngineExecutor() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean refresh() {
        InputStream inputStream;
        Properties properties = new Properties();
        //读取配置文件
        inputStream = RulesEngineExecutor.class.getClassLoader().getResourceAsStream("sql-analysis-rule-config.properties");

        try {
            assert inputStream != null;
            properties.load(new InputStreamReader(inputStream));
            inputStream.close();
        } catch (IOException e) {
            logger.error("规则引擎配置文件加载失败", e);
        }

        //规则集合
        Map<String, MVELRule> ruleMap = new HashMap<>();
        //评分规则集合
        ConcurrentHashMap<String, SqlScoreResultDetail> innerScoreMap = new ConcurrentHashMap<>();

        //读取配置内容
        properties.forEach((key, value) -> {
            String[] keyArr = StringUtils.split(key.toString(), ".");
            if (keyArr.length == 2) {
                //获取规则内容,如果规则命中后续操作将规则名称放入 ret 集合中
                MVELRule rule = ruleMap.getOrDefault(keyArr[0], new MVELRule().name(keyArr[0]).then("ret.add(\"" + keyArr[0] + "\")"));
                //获取评分规则,如果没有则重新创建一个评分规则对象
                SqlScoreResultDetail scoreRule = innerScoreMap.getOrDefault(keyArr[0], new SqlScoreResultDetail());
                switch (RuleFieldEnum.valueOf(keyArr[1].toUpperCase())) {
                    //如果是条件则加入 easyRule 规则中
                    case CONDITION:
                        rule.when(value.toString());
                        break;
                    //如果是操作则加入 easyRule 规则中
                    case ACTION:
                        rule.then(value.toString());
                        break;
                    //如果是名称则加入 easyRule 规则中
                    case NAME:
                        rule.name(value.toString());
                        break;
                    //如果是描述则加入 easyRule 规则中
                    case DESCRIPTION:
                        rule.description(value.toString());
                        break;
                    //如果是优先级则加入 easyRule 规则中
                    case PRIORITY:
                        rule.priority(NumberUtils.toInt(value.toString()));
                        break;
                    //减分 配置到评分规则对象中
                    case SCORE:
                        scoreRule.setScoreDeduction(NumberUtils.toInt(value.toString()));
                        break;
                    //原因 配置到评分规则对象中
                    case REASON:
                        scoreRule.setReason(value.toString());
                        break;
                    //建议 配置到评分规则对象中
                    case SUGGESTION:
                        scoreRule.setSuggestion(value.toString());
                        break;
                    //是否严格执行 配置到评分规则对象中
                    case STRICT:
                        scoreRule.setStrict(Boolean.valueOf(value.toString()));
                        break;
                }
                //将配置好的规则重新放入 ruleMap 中
                ruleMap.put(keyArr[0], rule);
                //将配置好的评分规则重新放入 innerScoreMap 中
                innerScoreMap.put(keyArr[0], scoreRule);
            }

        });
        //创建规则列表 将上面配置的规则注册进去
        Rules newRules = new Rules();
        for (Map.Entry<String, MVELRule> ruleEntry : ruleMap.entrySet()) {
            newRules.register(ruleEntry.getValue());
        }
        //规则创建持久化,后续交由执行时来进行使用
        rules = newRules;
        scoreMap = innerScoreMap;
        logger.info("规则引擎配置文件加载成功");
        return true;
    }

    public static List<SqlScoreResultDetail> execute(SqlAnalysisResult sqlAnalysisResult) {
        //评分结果
        ArrayList<SqlScoreResultDetail> retList = new ArrayList<>();
        //初始化返回规则
        ArrayList<String> ret = new ArrayList<>();

        //创建事实
        Facts facts = new Facts();
        facts.put("param", sqlAnalysisResult);
        facts.put("ret", ret);

        // 执行规则
        RULES_ENGINE.fire(rules, facts);

        //获取执行完成的结果
        ret = facts.get("ret");
        for (String ruleName : ret) {
            if (scoreMap.get(ruleName) != null){
                retList.add(scoreMap.get(ruleName));
            }
        }

        return retList;
    }

}
