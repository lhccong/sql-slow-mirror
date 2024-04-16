package com.cong.sql.slowmirror.analysis;

import com.cong.sql.slowmirror.config.MysqlVersion;
import com.cong.sql.slowmirror.extract.SqlExtractResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL 分析
 *
 * @author cong
 * @date 2024/04/15
 */
public class SqlAnalysis {

    private SqlAnalysis() {
        throw new IllegalStateException("Utility class");
    }
    private static final Logger logger = LoggerFactory.getLogger(SqlAnalysis.class);

    /**
     * mysql 版本标识
     */
    private static String mysqlVersion;

    public static SqlAnalysisResultList analysis(SqlExtractResult sqlExtractResult, Connection connection){
        if(sqlExtractResult==null){
            return null;
        }
        logger.info("====开启 SQL 分析====");

        List<SqlAnalysisResult> resultList = new ArrayList<>();
        SqlAnalysisResult sqlAnalysisResutlDto = null;

        //1、获取分析 SQL
        String sourceSql = sqlExtractResult.getSourceSql();
        //2、包装分析语句 添加 explain 前缀
        String analysisSql = getAnalysisSql(sourceSql);

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(analysisSql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                sqlAnalysisResutlDto = convertSqlAnalysisResultDto(rs);
                resultList.add(sqlAnalysisResutlDto);
            }
        } catch (SQLException e) {
            logger.error("SQL 执行异常");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                logger.error("SQL 关闭异常");
            }
        }
        logger.info("SQL 分析结果 = {}", sqlAnalysisResutlDto);

        SqlAnalysisResultList sqlAnalysisResultList = new SqlAnalysisResultList();
        sqlAnalysisResultList.setResultList(resultList);
        return sqlAnalysisResultList;
    }

    /**
     * 获取分析 SQL
     * 获取sql分析语句
     *
     * @param sql 拦截前的sql
     * @return {@link String}
     */
    private static String getAnalysisSql(String sql) {
        sql = "explain " + sql;
        return sql;
    }

    /**
     * 分析结果转换 分析结果dto
     *
     * @param resultSet 结果集
     * @return {@link SqlAnalysisResult}
     */
    private static SqlAnalysisResult convertSqlAnalysisResultDto(ResultSet resultSet) {
        SqlAnalysisResult sqlAnalysisResult = new SqlAnalysisResult();
        if(resultSet == null){
            return null;
        }
        try {
            if(StringUtils.isBlank(mysqlVersion)){
                mysqlVersion = getMysqlVersion(resultSet);
            }
            Long id = resultSet.getLong("id");
            String selectType = resultSet.getString("select_type");
            String table = resultSet.getString("table");
            String type = resultSet.getString("type");
            String possibleKeys = resultSet.getString("possible_keys");
            String key = resultSet.getString("key");
            String keyLen = resultSet.getString("key_len");
            String ref = resultSet.getString("ref");
            String rows = resultSet.getString("rows");
            String extra = resultSet.getString("Extra");

            sqlAnalysisResult.setId(id);
            sqlAnalysisResult.setSelectType(selectType);
            sqlAnalysisResult.setTable(table);
            sqlAnalysisResult.setType(type);
            sqlAnalysisResult.setPossibleKeys(possibleKeys);
            sqlAnalysisResult.setKey(key);
            sqlAnalysisResult.setKeyLen(keyLen);
            sqlAnalysisResult.setRef(ref);
            sqlAnalysisResult.setRows(rows);
            sqlAnalysisResult.setExtra(extra);
            if(mysqlVersion.equals(MysqlVersion.MYSQL_5_7.getVersion())){
                Double filtered = resultSet.getDouble("filtered");
                String partitions = resultSet.getString("partitions");
                sqlAnalysisResult.setPartitions(partitions);
                sqlAnalysisResult.setFiltered(filtered);
            }

        } catch (SQLException e) {
            logger.error("SQL 分析转换异常",e);
        }
        return sqlAnalysisResult;
    }

    /**
     * 获取 MySQL 版本
     *
     * @param rs RS
     * @return {@link String}
     */
    public static String getMysqlVersion(ResultSet rs){
        //根据返回字段数识别5.6 或者 5.7以上版本
        String mysqlVersion = MysqlVersion.MYSQL_5_6.getVersion();
        try {
            int columnCount = rs.getMetaData().getColumnCount();
            if(columnCount>10){
                mysqlVersion = MysqlVersion.MYSQL_5_7.getVersion();
            }
        } catch (Exception e) {
            logger.error("SQL 分析 获取 MySQL 版本异常",e);
        }
        return mysqlVersion;
    }
}
