package com.cong.sql.slowmirror.out;

import lombok.Getter;

/**
 * 输出模型枚举
 *
 * @author cong
 * @date 2024/04/16
 */
@Getter
public enum OutModelEnum {
    LOG("LOG", "日志方式输出"),
    MQ("MQ", "发送mq"),
    MYSQL("MYSQL", "mysql表存储");



    OutModelEnum(String modelType, String modelDesc) {
        this.modelType = modelType;
        this.modelDesc = modelDesc;
    }

    /**
     * 模式类型
     */
    private final String modelType;

    /**
     * 模式描述
     */
    private final String modelDesc;

}
