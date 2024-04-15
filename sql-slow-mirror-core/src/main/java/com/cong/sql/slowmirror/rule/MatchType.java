package com.cong.sql.slowmirror.rule;

import lombok.Getter;

/**
 * 匹配类型
 *
 * @author cong
 * @date 2024/04/15
 */
@Getter
public enum MatchType {
    EQUAL("等于"),
    GREATER("大于"),
    LESS("小于"),
    CONTAIN("包含");

    /**
     * 匹配类型
     */
    private final String type;

    MatchType(String type){
        this.type = type;
    }

}
