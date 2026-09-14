package com.example.recordz.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
@EnableCaching
public class JooqConfig {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(dbUser);
        config.setPassword(dbPassword);

        // Dimensionnement du pool : reste le vrai goulot d'étranglement
        // même avec les virtual threads (limité par la capacité MySQL, pas par le coût des threads)
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setPoolName("recordz-hikari-pool");

        // Evite le pinning des virtual threads sur les carrier threads
        // pendant l'attente d'une connexion (corrigé depuis HikariCP 5.1+)
        config.setConnectionTimeout(30_000);

        return new HikariDataSource(config);
    }

    @Bean
    public DSLContext dslContext(DataSource dataSource) {
        // Proxy transactionnel Spring pour que jOOQ respecte @Transactional
        DataSource proxiedDataSource = new TransactionAwareDataSourceProxy(dataSource);
        DataSourceConnectionProvider connectionProvider =
                new DataSourceConnectionProvider(proxiedDataSource);

        DefaultConfiguration jooqConfig = new DefaultConfiguration();
        jooqConfig.set(connectionProvider);
        jooqConfig.set(SQLDialect.MYSQL);

        return DSL.using(jooqConfig);
    }
}