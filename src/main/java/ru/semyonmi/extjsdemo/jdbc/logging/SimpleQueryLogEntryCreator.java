package ru.semyonmi.extjsdemo.jdbc.logging;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.DefaultQueryLogEntryCreator;

import java.util.List;
import java.util.Map;

import ru.semyonmi.extjsdemo.security.SecurityUtils;
import ru.semyonmi.extjsdemo.util.UuidUtils;

public class SimpleQueryLogEntryCreator extends DefaultQueryLogEntryCreator {

  private static final String QUID = "QUID";
  private static final String USER = "User";

  private static final ThreadLocal<String> uuidHolder = new ThreadLocal<>();

  protected String getBeforeLogEntry(ExecutionInfo execInfo,
                                     List<QueryInfo> queryInfoList,
                                     boolean writeDataSourceName,
                                     boolean writeAsJson) {
    // for performance purposes used notSecure
    uuidHolder.set(UuidUtils.notSecureRandomUuid().toString());
    boolean isBefore = true;
    return writeAsJson
      ? getLogEntryAsJson(execInfo, queryInfoList, writeDataSourceName, isBefore)
      : getLogEntry(execInfo, queryInfoList, writeDataSourceName, isBefore);
  }

  protected String getAfterLogEntry(ExecutionInfo execInfo,
                                    List<QueryInfo> queryInfoList,
                                    boolean writeDataSourceName,
                                    boolean writeAsJson) {
    boolean isBefore = false;
    String entry = writeAsJson ? getLogEntryAsJson(execInfo, queryInfoList, writeDataSourceName, isBefore)
      : getLogEntry(execInfo, queryInfoList, writeDataSourceName, isBefore);
    uuidHolder.remove();
    return entry;
  }

  protected String getLogEntry(ExecutionInfo execInfo,
                               List<QueryInfo> queryInfoList,
                               boolean writeDataSourceName,
                               boolean isBefore) {
    final StringBuilder sb = new StringBuilder();

    if (writeDataSourceName) {
      writeDataSourceNameEntry(sb, execInfo, queryInfoList);
    }

    // UUID
    writeUuidEntry(sb);

    if (isBefore) {
      // User
      writeUserEntry(sb);

      // Queries
      writeQueriesEntry(sb, execInfo, queryInfoList);

      if (execInfo.getBatchSize() > 0) {
        // BatchSize
        writeBatchSizeEntry(sb, execInfo, queryInfoList);
      } else {
        // Params
        writeParamsEntry(sb, execInfo, queryInfoList);
      }

      chompIfEndWith(sb, ' ');
      chompIfEndWith(sb, ',');

      return sb.toString();
    }

    // Time
    writeTimeEntry(sb, execInfo, queryInfoList);

    // Success
    writeResultEntry(sb, execInfo, queryInfoList);

    return sb.toString();
  }

  protected String getLogEntryAsJson(ExecutionInfo execInfo,
                                     List<QueryInfo> queryInfoList,
                                     boolean writeDataSourceName,
                                     boolean isBefore) {
    StringBuilder sb = new StringBuilder();

    sb.append("{");
    if (writeDataSourceName) {
      writeDataSourceNameEntryForJson(sb, execInfo, queryInfoList);
    }

    // UUID
    writeUuidEntryForJson(sb);

    if (isBefore) {
      // User
      writeUserEntryForJson(sb);

      // Queries
      writeQueriesEntryForJson(sb, execInfo, queryInfoList);

      // Params
      writeParamsEntryForJson(sb, execInfo, queryInfoList);

      return sb.toString();
    }

    // Time
    writeTimeEntryForJson(sb, execInfo, queryInfoList);

    // Success
    writeResultEntryForJson(sb, execInfo, queryInfoList);

    return sb.toString();
  }

  /**
   * Write UUID query.
   *
   * @param sb StringBuilder to write
   */
  protected void writeUuidEntry(StringBuilder sb) {
    sb.append(QUID);
    sb.append(':');
    sb.append(uuidHolder.get());
    sb.append(", ");
  }

  /**
   * Write user query.
   *
   * @param sb StringBuilder to write
   */
  protected void writeUserEntry(StringBuilder sb) {
    sb.append(USER);
    sb.append(':');
    sb.append(escapeSpecialCharacterForJson(SecurityUtils.getUser()));
    sb.append(", ");
  }

  /**
   * Write UUID query.
   *
   * @param sb StringBuilder to write
   */
  protected void writeUuidEntryForJson(StringBuilder sb) {
    sb.append('\"');
    sb.append(QUID);
    sb.append("\":\"");
    sb.append(uuidHolder.get());
    sb.append("\", ");
  }

  /**
   * Write user query.
   *
   * @param sb StringBuilder to write
   */
  protected void writeUserEntryForJson(StringBuilder sb) {
    sb.append('\"');
    sb.append(USER);
    sb.append("\":\"");
    sb.append(SecurityUtils.getUser());
    sb.append("\", ");
  }

  @Override
  protected void writeResultEntry(StringBuilder sb, ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
    sb.append("Success:");
    sb.append(execInfo.isSuccess() ? "True" : "False");
  }

  @Override
  protected void writeResultEntryForJson(StringBuilder sb, ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
    sb.append("\"success\":");
    sb.append(execInfo.isSuccess() ? "true" : "false");
    sb.append("}");
  }

  @Override
  protected void writeParamsEntry(StringBuilder sb, ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
    for (QueryInfo queryInfo : queryInfoList) {
      for (Map<String, Object> paramMap : queryInfo.getQueryArgsList()) {
        if (!paramMap.isEmpty()) {
          super.writeParamsEntry(sb, execInfo, queryInfoList);
          return;
        }
      }
    }
  }
}
