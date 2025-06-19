package com.test.importexcelsqlserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DatabaseConfig {
    private static JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    public static JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://localhost:3306/ordercode?useSSL=false&serverTimezone=UTC");
            dataSource.setUsername("root");
            dataSource.setPassword("123456");
            jdbcTemplate = new JdbcTemplate(dataSource);
            logger.info("数据库连接成功");
        }
        return jdbcTemplate;
    }
} 