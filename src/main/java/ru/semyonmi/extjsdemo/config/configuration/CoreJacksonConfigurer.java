package ru.semyonmi.extjsdemo.config.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.text.DateFormat;

import ru.semyonmi.extjsdemo.util.ISO8601DateFormatWithMillis;

@Configuration
public class CoreJacksonConfigurer {

  /**
   * Default object mapper, used everywhere.
   */
  @Bean
  @ConditionalOnMissingBean
  @Primary
  public ObjectMapper objectMapper() {
    return disableFlushAfterWriteValue(defaultObjectMapper());
  }

  /**
   * Object mapper, used for update purposes.<br/>
   * Unlike default object mapper: disabled {@link com.fasterxml.jackson.databind.MapperFeature#DEFAULT_VIEW_INCLUSION}.
   */
  @Bean
  public ObjectMapper updateObjectMapper() {
    return disableFlushAfterWriteValue(defaultObjectMapper())
      .disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
  }

  /**
   * Object mapper, used for WebSocket.<br/>
   * Unlike default object mapper: enabled {@link com.fasterxml.jackson.databind.SerializationFeature#FLUSH_AFTER_WRITE_VALUE}.
   */
  @Bean
  public ObjectMapper socketObjectMapper() {
    return defaultObjectMapper();
  }

  /**
   * Allow to catch HttpMessageNotWritableException and to replace the error.
   */
  private ObjectMapper disableFlushAfterWriteValue(ObjectMapper objectMapper) {
    return objectMapper.disable(SerializationFeature.FLUSH_AFTER_WRITE_VALUE);
  }

  private ObjectMapper defaultObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();

    DateFormat dateFormat = new ISO8601DateFormatWithMillis();
    objectMapper.setDateFormat(dateFormat);

    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    objectMapper.registerModule(new JodaModule());
    return objectMapper;
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
