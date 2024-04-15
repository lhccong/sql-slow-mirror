package com.cong.sql.slowmirror.config;

import lombok.Getter;

/**
 * MySQL版本
 *
 * @author cong
 * @date 2024/04/15
 */
@Getter
public enum MysqlVersion {

    MYSQL_5_6("MYSQL_5.6"),
    MYSQL_5_7( "MYSQL_5.7");


    MysqlVersion(String version) {
        this.version = version;
    }

    private final String version;

}
