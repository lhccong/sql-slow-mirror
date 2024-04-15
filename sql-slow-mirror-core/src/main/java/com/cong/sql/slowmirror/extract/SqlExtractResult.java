package com.cong.sql.slowmirror.extract;

import lombok.Data;

/**
 * SQL 提取结果
 *
 * @author cong
 * @date 2024/04/15
 */
@Data
public class SqlExtractResult {

    /**
     * 基于mybatis 配置的sql id
     */
    private String sqlId;

    /**
     * 待执行，原sql
     */
    private String sourceSql;


}
