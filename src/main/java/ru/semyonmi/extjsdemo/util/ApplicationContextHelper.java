package ru.semyonmi.extjsdemo.util;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHelper implements ApplicationContextAware, BeanFactoryPostProcessor {

  private static final Logger logger = LoggerFactory.getLogger(ApplicationContextHelper.class);

  private static final ApplicationContextHelper INSTANCE = new ApplicationContextHelper();
  private static ApplicationContext applicationContext;
  public static ConfigurableListableBeanFactory beanFactory;

  private ApplicationContextHelper() {
  }

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) {
    ApplicationContextHelper.applicationContext = applicationContext;
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    ApplicationContextHelper.beanFactory = beanFactory;
  }

  public static ApplicationContextHelper getInstance() {
    return INSTANCE;
  }

  /**
   * Get bean by {@code name}.
   *
   * @param name name of the bean
   * @return bean
   */
  public static Object getBean(String name) {
    if (name == null) {
      return null;
    }

    try {
      return applicationContext.getBean(name);
    } catch (NoSuchBeanDefinitionException ignore) {
      logger.warn("Bean " + name + " not found");
    } catch (Exception ignore) {
      logger.warn(ignore.getMessage());
    }
    return null;
  }

  /**
   * Get bean by {@code name} and cast to {@link Class}.
   *
   * @param name         name of the bean
   * @param requiredType type of the bean
   * @return bean
   */
  public static <T> T getBean(String name, Class<T> requiredType) {
    if (name == null) {
      return null;
    }

    try {
      return applicationContext.getBean(name, requiredType);
    } catch (NoSuchBeanDefinitionException ignore) {
      logger.warn("Bean " + name + " not found");
    } catch (BeanNotOfRequiredTypeException ignore) {
      logger.warn("Bean " + name + " is not of required type " + requiredType.getName());
    } catch (Exception ignore) {
      logger.warn(ignore.getMessage());
    }
    return null;
  }

  /**
   * Get bean by {@link Class}.
   *
   * @param requiredType type of the bean
   * @return bean
   */
  public static <T> T getBean(Class<T> requiredType) {
    try {
      return applicationContext.getBean(requiredType);
    } catch (NoUniqueBeanDefinitionException ignore) {
      logger.warn("Multiple beans with required type " + requiredType.getName());
    } catch (NoSuchBeanDefinitionException ignore) {
      logger.warn("Bean is not of required type " + requiredType.getName());
    } catch (Exception ignore) {
      logger.warn(ignore.getMessage());
    }
    return null;
  }

  public static Object getBeanOrDefault(String name, String defaultName) {
    return ObjectUtils.firstNonNull(getBean(name), getBean(defaultName));
  }

  public static <T> T getBeanOrDefault(String name, String defaultName, Class<T> requiredType) {
    return ObjectUtils.firstNonNull(getBean(name, requiredType), getBean(defaultName, requiredType));
  }

  public static <T> T getBeanOrDefault(String name, Class<T> requiredType) {
    return ObjectUtils.firstNonNull((T) getBean(name), getBean(requiredType));
  }
}
