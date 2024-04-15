package com.cong.sql.slowmirror.analysis;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SQL 分析 SQL 类型枚举
 *
 * @author cong
 * @date 2024/04/15
 */
@Getter
public enum SqlAnalysisSqlTypeEnum {

    SELECT("查询", "SELECT"),
    UPDATE("更新", "UPDATE"),
    INSERT("插入", "INSERT"),
    DELETE("删除", "DELETE");
    /**
     * sql类型
     */
    private final String text;

    /**
     * 描述
     */
    private final String value;

    SqlAnalysisSqlTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值
     * 获取值列表
     *
     * @return {@link List}<{@link String}>
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 按值获取枚举
     * 根据 value 获取枚举
     *
     * @param value 价值
     * @return {@link SqlAnalysisSqlTypeEnum}
     */
    public static SqlAnalysisSqlTypeEnum getEnumByValue(String value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (SqlAnalysisSqlTypeEnum anEnum : SqlAnalysisSqlTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }



}
