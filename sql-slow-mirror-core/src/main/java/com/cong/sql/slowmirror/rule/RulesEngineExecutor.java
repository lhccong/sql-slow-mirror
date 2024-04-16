package com.cong.sql.slowmirror.rule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 规则引擎执行程序
 *
 * @author cong
 * @date 2024/04/16
 */
public class RulesEngineExecutor implements SqlScoreRuleLoader {

    private static final Logger logger = LoggerFactory.getLogger(RulesEngineExecutor.class);
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
            logger.error("规则引擎配置文件加载失败",e);
        }
        return true;
    }

    @Override
    public boolean loadScoreRule() {
        return false;
    }
}
