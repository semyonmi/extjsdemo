package ru.semyonmi.extjsdemo.config.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.Closeable;
import java.util.Arrays;

import javax.sql.DataSource;

import ru.semyonmi.extjsdemo.config.properties.CoreProperties;
import ru.semyonmi.extjsdemo.jdbc.logging.CloseableProxyDataSourceBuilder;

@Configuration
@EnableTransactionManagement
public class CoreDatabaseConfiguration {

  private final Logger logger = LoggerFactory.getLogger(CoreDatabaseConfiguration.class);

  @Autowired
  private Environment env;

  @Bean(destroyMethod = "close")
  @ConditionalOnMissingBean
  public DataSource dataSource(DataSourceProperties dataSourceProperties, CoreProperties coreProperties) {
    DataSource dataSource = getDataSource(dataSourceProperties, coreProperties, "default");

    logger.debug("DataSource(s) configured. The next step can be slow: Hibernate initializes the metadata " +
      "according to the scheme...(especially on remote databases)");

    return dataSource;
  }

  private DataSource getDataSource(DataSourceProperties dataSourceProperties, CoreProperties coreProperties,
                                   String name) {
    logger.debug("Configuring Datasource: {}", name);

    String url = dataSourceProperties.getUrl();
    if (url == null) {
      logger.error("Your database connection pool configuration is incorrect! The application" +
          " cannot start. Please check your Spring profile, current profiles are: {}",
        Arrays.toString(env.getActiveProfiles()));

      throw new ApplicationContextException("Database connection pool is not configured correctly");
    }

    HikariConfig config = new HikariConfig();

    config.setDriverClassName(dataSourceProperties.getDriverClassName());
    config.setJdbcUrl(url);
    config.setUsername(dataSourceProperties.getUsername());
    config.setPassword(dataSourceProperties.getPassword());

    config.setMaximumPoolSize(coreProperties.getPool().getMaximumPoolSize());
    config.setMinimumIdle(0);
    config.setIdleTimeout(coreProperties.getPool().getIdleTimeout()); // millis
    config.setConnectionTimeout(coreProperties.getPool().getConnectionTimeout()); // millis
    config.setConnectionTestQuery("SELECT 1 FROM DUAL");

    HikariDataSource hikariDataSource = new HikariDataSource(config);
    return wrapDataSourceLogging(hikariDataSource);
  }

//  @Autowired
//  private DataSource dataSource;
//
//  @Autowired
//  private JdbcTemplate jdbcTemplate;
//
//  @Bean
//  public JdbcTemplate jdbcTemplate() {
//    return new JdbcTemplate(dataSource);
//  }
//
//  @Bean
//  public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
//    return new NamedParameterJdbcTemplate(jdbcTemplate);
//  }

  public static <T extends DataSource & Closeable> DataSource wrapDataSourceLogging(T baseDataSource) {
    return wrapDataSourceLogging(baseDataSource, "dataSource");
  }

  /**
   * wrapDataSourceLogging.
   */
  public static <T extends DataSource & Closeable> DataSource wrapDataSourceLogging(T baseDataSource,
                                                                                    String dataSourceName) {
    return CloseableProxyDataSourceBuilder
            .create(baseDataSource)
            .name(dataSourceName)
            .logQueryBySlf4j()
            //.asJson()
            .countQuery()// collect query metrics
            .build();
  }
}
