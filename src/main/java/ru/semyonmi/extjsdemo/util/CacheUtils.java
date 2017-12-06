package ru.semyonmi.extjsdemo.util;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class CacheUtils {

  /**
   * Clear all caches.
   */
  public static void clearAll(CacheManager cacheManager) {
    cacheManager.getCacheNames().stream().forEach(cacheName -> {
      try {
        cacheManager.getCache(cacheName).clear();
      } catch (Exception e) {
        throw new RuntimeException("Could not clear cache " + cacheName, e);
      }
    });
  }

  /**
   * {@link org.springframework.cache.Cache#get(Object)}.
   */
  public static Cache.ValueWrapper get(CacheManager cacheManager, String cacheName, Object key) {
    Cache cache = cacheManager.getCache(cacheName);
    Assert.notNull(cache, "Required cache with name \"" + cacheName + "\" not found");
    return cache.get(key);
  }

  /**
   * {@link org.springframework.cache.Cache#get(Object, Class)}.
   */
  public static <T> T get(CacheManager cacheManager, String cacheName, Object key, Class<T> type) {
    Cache cache = cacheManager.getCache(cacheName);
    Assert.notNull(cache, "Required cache with name \"" + cacheName + "\" not found");
    return cache.get(key, type);
  }

  /**
   * Returns the values of all specified keys.
   */
  public static <K, V> Map<K, V> getAll(Cache cache, Set<K> keys, Class<V> vClass) {
    Assert.notNull(cache, "Cache is required");
    Assert.notNull(keys, "Keys is required for " + vClass);

    Map<K, V> map = new HashMap<>();
    for (K key : keys) {
      V value = cache.get(key, vClass);
      //element may not be in the cache
      if (value == null) {
        continue;
      }
      map.put(key, value);
    }
    return map;
  }

  /**
   * Check that {@code key} exists in cache with {@code cacheName}.
   */
  public static boolean isKeyInCache(CacheManager cacheManager, String cacheName, Object key) {
    return get(cacheManager, cacheName, key) != null;
  }

  /**
   * Get {@link org.springframework.cache.Cache} by {@code cacheName}.
   */
  public static Cache getCache(CacheManager cacheManager, String cacheName) {
    return cacheManager.getCache(cacheName);
  }

  /**
   * Get value from {@link org.springframework.cache.Cache} by {@code cacheName} and {@code key}.
   * If value not exists then logon it from {@code valueSupplier} and put to {@link org.springframework.cache.Cache}.
   */
  public static <T, K> T computeIfAbsent(CacheManager cacheManager, String cacheName, K key, Class<T> type,
                                      Supplier<T> valueSupplier) {
    Cache cache = cacheManager.getCache(cacheName);
    Assert.notNull(cache, "Required cache with name \"" + cacheName + "\" not found");

    T value = cache.get(key, type);
    if (value != null) {
      return value;
    }

    value = valueSupplier.get();
    cache.put(key, value);
    return value;
  }

  /**
   * Locks cache if needed (used for cache invalidation).
   *
   * @param cacheManager cache manager
   * @return true if cache is successfully locked
   */
  public static boolean lockCache(CacheManager cacheManager) {
    return true;
  }

  /**
   * Unlocks cache if needed (used for cache invalidation).
   *
   * @param cacheManager cache manager
   */
  public static void unlockCache(CacheManager cacheManager) {}
}
