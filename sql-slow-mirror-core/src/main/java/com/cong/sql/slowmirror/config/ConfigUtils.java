package com.cong.sql.slowmirror.config;

import com.cong.sql.slowmirror.utils.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;
import java.util.Properties;

/**
 * 配置工具类
 *
 * @author cong
 * @date 2024/03/07
 */
public class ConfigUtils {


    private ConfigUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 加载配置
     * 加载配置对象
     *
     * @param prefix 前缀
     */
    public static Properties loadConfig( String prefix) {
        return loadConfig(prefix, "");
    }

    /**
     * 加载配置
     * 加载配置对象，支持区分环境
     *
     * @param prefix      前缀
     * @param environment 环境
     */
    public static Properties loadConfig( String prefix, String environment) {
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StringUtils.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".yml");
        Yaml yaml=new Yaml();
        Map<String, Object> map =
                yaml.load(ConfigUtils.class.getClassLoader()
                        .getResourceAsStream(configFileBuilder.toString()));
        Map<String, Object> stringMap = MapUtils.getValueByPath(map, prefix);
        return MapUtils.convertMapToProperties(stringMap);
    }

}