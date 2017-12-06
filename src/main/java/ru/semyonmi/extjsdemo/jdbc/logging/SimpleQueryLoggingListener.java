package ru.semyonmi.extjsdemo.jdbc.logging;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.SLF4JQueryLoggingListener;

import org.slf4j.LoggerFactory;

import java.util.List;

public class SimpleQueryLoggingListener extends SLF4JQueryLoggingListener {

  protected final SimpleQueryLogEntryCreator queryLogEntryCreator;

  public SimpleQueryLoggingListener() {
    logger = LoggerFactory.getLogger(SimpleQueryLoggingListener.class);
    queryLogEntryCreator = new SimpleQueryLogEntryCreator();
  }

  @Override
  public void beforeQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
    String entry = queryLogEntryCreator.getBeforeLogEntry(execInfo, queryInfoList, writeDataSourceName, writeAsJson);
    writeLog(entry);
  }

  @Override
  public void afterQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
    String entry = queryLogEntryCreator.getAfterLogEntry(execInfo, queryInfoList, writeDataSourceName, writeAsJson);
    writeLog(entry);
  }
}
