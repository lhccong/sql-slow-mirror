package com.cong.sql.slowmirror.utils;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * 地图实用程序
 *
 * @author cong
 * @date 2024/04/15
 */
public class MapUtils {

    private MapUtils() {
        throw new IllegalStateException("Utility class");
    }
    // 根据键路径从嵌套 Map 中获取值
    public static Map<String, Object> getValueByPath(Map<String, Object> map, String keyPath) {
        // 按照 . 分割键路径
        String[] keys = keyPath.split("\\.");

        // 逐层深入 Map 结构
        Map<String, Object> currentMap = map;
        for (String key : keys) {
            if (currentMap.containsKey(key) && currentMap.get(key) instanceof Map) {
                currentMap = (Map<String, Object>) currentMap.get(key);
            } else {
                // 如果遇到非 Map 类型的值或键不存在，返回 null 或自定义默认值
                return Collections.emptyMap();
            }
        }

        // 返回最终的值
        return currentMap;
    }
    // 将 Map 转换为 Properties
    public static Properties convertMapToProperties(Map<String, Object> map) {
        Properties properties = new Properties();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            properties.setProperty(entry.getKey(), entry.getValue().toString());
        }
        return properties;
    }
}
