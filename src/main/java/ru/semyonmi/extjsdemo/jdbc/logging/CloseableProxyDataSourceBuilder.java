package ru.semyonmi.extjsdemo.jdbc.logging;

import net.ttddyy.dsproxy.listener.ChainListener;
import net.ttddyy.dsproxy.listener.CommonsLogLevel;
import net.ttddyy.dsproxy.listener.CommonsQueryLoggingListener;
import net.ttddyy.dsproxy.listener.DataSourceQueryCountListener;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.listener.SLF4JLogLevel;
import net.ttddyy.dsproxy.listener.SLF4JQueryLoggingListener;
import net.ttddyy.dsproxy.listener.SystemOutQueryLoggingListener;
import net.ttddyy.dsproxy.transform.ParameterTransformer;
import net.ttddyy.dsproxy.transform.QueryTransformer;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

/**
 * Extended version of {@link net.ttddyy.dsproxy.support.ProxyDataSourceBuilder}.
 * Used to build {@link CloseableProxyDataSource}.
 */
public class CloseableProxyDataSourceBuilder<T extends DataSource & Closeable> {

  private T dataSource;
  private String dataSourceName;

  private boolean createCommonsQueryListener;
  private CommonsLogLevel commonsLogLevel;
  private boolean createSlf4jQueryListener;
  private SLF4JLogLevel slf4JLogLevel;
  private String loggerName;
  private boolean createSysOutQueryListener;
  private boolean createDataSourceQueryCountListener;
  private boolean jsonFormat;
  private boolean writeDataSourceName;
  private List<QueryExecutionListener> queryExecutionListeners = new ArrayList<QueryExecutionListener>();

  private ParameterTransformer parameterTransformer;
  private QueryTransformer queryTransformer;

  public static CloseableProxyDataSourceBuilder create() {
    return new CloseableProxyDataSourceBuilder();
  }

  public static <T extends DataSource & Closeable> CloseableProxyDataSourceBuilder create(T dataSource) {
    return new CloseableProxyDataSourceBuilder(dataSource);
  }

  public static <T extends DataSource & Closeable> CloseableProxyDataSourceBuilder create(String dataSourceName,
                                                                                          T dataSource) {
    return new CloseableProxyDataSourceBuilder(dataSource).name(dataSourceName);
  }

  public CloseableProxyDataSourceBuilder() {
  }

  public CloseableProxyDataSourceBuilder(T dataSource) {
    this.dataSource = dataSource;
  }

  /**
   * Set actual datasource.
   *
   * @param dataSource actual datasource
   * @return builder
   */
  public CloseableProxyDataSourceBuilder dataSource(T dataSource) {
    this.dataSource = dataSource;
    return this;
  }

  /**
   * Register {@link net.ttddyy.dsproxy.listener.CommonsQueryLoggingListener}.
   *
   * @return builder
   */
  public CloseableProxyDataSourceBuilder logQueryByCommons() {
    this.createCommonsQueryListener = true;
    return this;
  }

  /**
   * Register {@link net.ttddyy.dsproxy.listener.CommonsQueryLoggingListener}.
   *
   * @param logLevel log level for commons
   * @return builder
   */
  public CloseableProxyDataSourceBuilder logQueryByCommons(CommonsLogLevel logLevel) {
    this.createCommonsQueryListener = true;
    this.commonsLogLevel = logLevel;
    return this;
  }

  /**
   * Register {@link net.ttddyy.dsproxy.listener.CommonsQueryLoggingListener}.
   *
   * @param loggerName logger name
   * @return builder
   * @since 1.3.1
   */
  public CloseableProxyDataSourceBuilder logQueryByCommons(String loggerName) {
    this.createCommonsQueryListener = true;
    this.loggerName = loggerName;
    return this;
  }

  /**
   * Register {@link net.ttddyy.dsproxy.listener.CommonsQueryLoggingListener}.
   *
   * @param logLevel   log level for commons
   * @param loggerName logger name
   * @return builder
   * @since 1.3.1
   */
  public CloseableProxyDataSourceBuilder logQueryByCommons(CommonsLogLevel logLevel, String loggerName) {
    this.createCommonsQueryListener = true;
    this.commonsLogLevel = logLevel;
    this.loggerName = loggerName;
    return this;
  }

  /**
   * Register {@link net.ttddyy.dsproxy.listener.SLF4JQueryLoggingListener}.
   *
   * @return builder
   */
  public CloseableProxyDataSourceBuilder logQueryBySlf4j() {
    this.createSlf4jQueryListener = true;
    return this;
  }

  /**
   * Register {@link net.ttddyy.dsproxy.listener.SLF4JQueryLoggingListener}.
   *
   * @param logLevel log level for slf4j
   * @return builder
   */
  public CloseableProxyDataSourceBuilder logQueryBySlf4j(SLF4JLogLevel logLevel) {
    this.createSlf4jQueryListener = true;
    this.slf4JLogLevel = logLevel;
    return this;
  }

  /**
   * Register {@link net.ttddyy.dsproxy.listener.SLF4JQueryLoggingListener}.
   *
   * @param loggerName logger name
   * @return builder
   * @since 1.3.1
   */
  public CloseableProxyDataSourceBuilder logQueryBySlf4j(String loggerName) {
    this.createSlf4jQueryListener = true;
    this.loggerName = loggerName;
    return this;
  }

  /**
   * Register {@link net.ttddyy.dsproxy.listener.SLF4JQueryLoggingListener}.
   *
   * @param logLevel   log level for commons
   * @param loggerName logger name
   * @return builder
   * @since 1.3.1
   */
  public CloseableProxyDataSourceBuilder logQueryBySlf4j(SLF4JLogLevel logLevel, String loggerName) {
    this.createSlf4jQueryListener = true;
    this.slf4JLogLevel = logLevel;
    this.loggerName = loggerName;
    return this;
  }

  /**
   * Register {@link net.ttddyy.dsproxy.listener.SystemOutQueryLoggingListener}.
   *
   * @return builder
   */
  public CloseableProxyDataSourceBuilder logQueryToSysOut() {
    this.createSysOutQueryListener = true;
    return this;

  }

  /**
   * Create {@link net.ttddyy.dsproxy.listener.DataSourceQueryCountListener}.
   *
   * @return builder
   */
  public CloseableProxyDataSourceBuilder countQuery() {
    this.createDataSourceQueryCountListener = true;
    return this;
  }

  /**
   * Register given listener.
   *
   * @param listener a listener to register
   * @return builder
   */
  public CloseableProxyDataSourceBuilder listener(QueryExecutionListener listener) {
    this.queryExecutionListeners.add(listener);
    return this;
  }

  /**
   * Format logging output as JSON.
   *
   * @return builder
   */
  public CloseableProxyDataSourceBuilder asJson() {
    this.jsonFormat = true;
    return this;
  }

  /**
   * Write data source name.
   *
   * @return builder
   */
  public CloseableProxyDataSourceBuilder writeDataSourceName() {
    this.writeDataSourceName = true;
    return this;
  }

  /**
   * Set datasource name.
   *
   * @param dataSourceName datasource name
   * @return builder
   */
  public CloseableProxyDataSourceBuilder name(String dataSourceName) {
    this.dataSourceName = dataSourceName;
    return this;
  }

  /**
   * Register {@link net.ttddyy.dsproxy.transform.QueryTransformer}.
   *
   * @param queryTransformer a query-transformer to register
   * @return builder
   */
  public CloseableProxyDataSourceBuilder queryTransformer(QueryTransformer queryTransformer) {
    this.queryTransformer = queryTransformer;
    return this;
  }

  /**
   * Register {@link net.ttddyy.dsproxy.transform.ParameterTransformer}.
   *
   * @param parameterTransformer a query-parameter-transformer to register
   * @return builder
   */
  public CloseableProxyDataSourceBuilder parameterTransformer(ParameterTransformer parameterTransformer) {
    this.parameterTransformer = parameterTransformer;
    return this;
  }

  /**
   * Build {@link CloseableProxyDataSource}.
   *
   * @return newly created data source
   */
  public CloseableProxyDataSource build() {
    CloseableProxyDataSource proxyDataSource = new CloseableProxyDataSource();

    if (this.dataSource != null) {
      proxyDataSource.setDataSource(dataSource);
    }

    // DataSource Name
    if (this.dataSourceName != null) {
      proxyDataSource.setDataSourceName(dataSourceName);
    }

    // Query Logging Listeners
    List<QueryExecutionListener> listeners = new ArrayList<QueryExecutionListener>();
    if (this.createCommonsQueryListener) {
      CommonsQueryLoggingListener listener = new CommonsQueryLoggingListener();
      if (this.commonsLogLevel != null) {
        listener.setLogLevel(this.commonsLogLevel);
      }
      if (this.loggerName != null && !this.loggerName.isEmpty()) {
        listener.setLoggerName(this.loggerName);
      }
      listener.setWriteAsJson(this.jsonFormat);
      listener.setWriteDataSourceName(this.writeDataSourceName);
      listeners.add(listener);
    }
    if (this.createSlf4jQueryListener) {
      SLF4JQueryLoggingListener listener = new SimpleQueryLoggingListener();
      if (this.slf4JLogLevel != null) {
        listener.setLogLevel(this.slf4JLogLevel);
      }
      if (this.loggerName != null && !this.loggerName.isEmpty()) {
        listener.setLoggerName(this.loggerName);
      }
      listener.setWriteAsJson(this.jsonFormat);
      listener.setWriteDataSourceName(this.writeDataSourceName);
      listeners.add(listener);
    }
    if (this.createSysOutQueryListener) {
      SystemOutQueryLoggingListener listener = new SystemOutQueryLoggingListener();
      listener.setWriteAsJson(this.jsonFormat);
      listener.setWriteDataSourceName(this.writeDataSourceName);
      listeners.add(listener);
    }

    // countQuery listener
    if (this.createDataSourceQueryCountListener) {
      listeners.add(new DataSourceQueryCountListener());
    }

    // explicitly added listeners
    listeners.addAll(this.queryExecutionListeners);

    if (!listeners.isEmpty()) {
      if (listeners.size() == 1) {
        proxyDataSource.setListener(listeners.get(0));
      } else {
        ChainListener chainListener = new ChainListener();
        chainListener.getListeners().addAll(listeners);
        proxyDataSource.setListener(chainListener);
      }
    }

    if (this.queryTransformer != null) {
      proxyDataSource.getInterceptorHolder().setQueryTransformer(this.queryTransformer);
    }
    if (this.parameterTransformer != null) {
      proxyDataSource.getInterceptorHolder().setParameterTransformer(this.parameterTransformer);
    }

    return proxyDataSource;
  }
}
