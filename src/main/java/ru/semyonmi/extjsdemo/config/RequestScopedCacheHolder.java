package ru.semyonmi.extjsdemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestScopedCacheHolder {
  private static final Logger logger = LoggerFactory.getLogger(RequestScopedCacheHolder.class);

  private CacheManager cacheManager;
  private Map<String, StatCount> statCountMap = new HashMap<>();

  @PostConstruct
  public void createCacheManager() {
    this.cacheManager = makeCacheManager();
  }

  public CacheManager getCacheManager() {
    return cacheManager;
  }

  /**
   * getStat.
   */
  public StatCount getStat(String key) {
    return statCountMap.computeIfAbsent(key, (s)-> new StatCount());
  }

  private CacheManager makeCacheManager() {
    return new ConcurrentMapCacheManager();
  }

  public void clear() {
    cacheManager.getCacheNames().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
  }

  /**
   * destroy.
   */
  @PreDestroy
  public void destroy() {
    if (!statCountMap.isEmpty()) {
      String statistic = statCountMap.keySet().stream()
        .filter(key -> statCountMap.get(key).getCount() > 1)
        .map(key -> key + ": " + statCountMap.get(key).getCount())
        .collect(Collectors.joining("; "));
      if (!statistic.isEmpty()) {
        logger.debug("Scope request cache stats - " + statistic);
      }
    }

    statCountMap.clear();
    cacheManager = null;
  }

  public static class StatCount {
    private int count;

    public void increment() {
      count++;
    }

    public int getCount() {
      return count;
    }
  }
}
