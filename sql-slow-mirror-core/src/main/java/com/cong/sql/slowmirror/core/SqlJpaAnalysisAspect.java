package com.cong.sql.slowmirror.core;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;

@Slf4j
public class SqlJpaAnalysisAspect extends EmptyInterceptor {

    @Override
    public String onPrepareStatement(String sql) {
        // 在这里拦截和处理SQL语句
        log.info("Intercepted SQL: {}", sql);
        return super.onPrepareStatement(sql);
    }
}
