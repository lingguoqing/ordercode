package com.test.importexcelsqlserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseConfig {
    private static JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    public static JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            try (InputStream in = DatabaseConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
                Properties props = new Properties();
                props.load(in);
                DriverManagerDataSource dataSource = new DriverManagerDataSource();
                dataSource.setDriverClassName(props.getProperty("db.driverClassName"));
                dataSource.setUrl(props.getProperty("db.url"));
                dataSource.setUsername(props.getProperty("db.username"));
                dataSource.setPassword(props.getProperty("db.password"));
                jdbcTemplate = new JdbcTemplate(dataSource);
                logger.info("数据库连接成功");
            } catch (Exception e) {
                logger.error("数据库连接失败", e);
            }
        }
        return jdbcTemplate;
    }
} 