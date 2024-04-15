package com.cong.sql.slowmirror.rule;

import lombok.Getter;

/**
 * 匹配列
 *
 * @author cong
 * @date 2024/04/15
 */
@Getter
public enum MatchColumn {
    SELECT_TYPE("selectType"),
    TABLE("table"),
    PARTITIONS("partitions"),
    TYPE("type"),
    POSSIBLE_KEYS("possibleKeys"),
    KEY("key"),
    KEY_LEN("keyLen"),
    REF("ref"),
    ROWS("rows"),
    FILTERED("filtered"),
    EXTRA("extra");

    /**
     * 匹配字段
     */
    private final String column;

    MatchColumn(String column){
        this.column = column;
    }

}
