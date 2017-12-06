package ru.semyonmi.extjsdemo.config.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

import ru.semyonmi.extjsdemo.security.expression.EDOAuth2MethodSecurityExpressionHandler;

@Configuration
@ConditionalOnClass({ OAuth2AccessToken.class })
@ConditionalOnBean(GlobalMethodSecurityConfiguration.class)
public class OAuth2MethodSecurityConfiguration
        implements BeanFactoryPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
            throws BeansException {
        OAuth2ExpressionHandlerInjectionPostProcessor processor = new OAuth2ExpressionHandlerInjectionPostProcessor(
                this.applicationContext);
        beanFactory.addBeanPostProcessor(processor);
    }

    private static class OAuth2ExpressionHandlerInjectionPostProcessor
            implements BeanPostProcessor {

        private ApplicationContext applicationContext;

        OAuth2ExpressionHandlerInjectionPostProcessor(
                ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName)
                throws BeansException {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName)
                throws BeansException {
            if (bean instanceof DefaultMethodSecurityExpressionHandler
                && !(bean instanceof OAuth2MethodSecurityExpressionHandler)) {
                return getExpressionHandler(
                        (DefaultMethodSecurityExpressionHandler) bean);
            }
            return bean;
        }

        private OAuth2MethodSecurityExpressionHandler getExpressionHandler(
                DefaultMethodSecurityExpressionHandler bean) {
            OAuth2MethodSecurityExpressionHandler handler = new EDOAuth2MethodSecurityExpressionHandler();
            handler.setApplicationContext(this.applicationContext);
            AuthenticationTrustResolver trustResolver = findInContext(
                    AuthenticationTrustResolver.class);
            if (trustResolver != null) {
                handler.setTrustResolver(trustResolver);
            }
            PermissionEvaluator permissions = findInContext(PermissionEvaluator.class);
            if (permissions != null) {
                handler.setPermissionEvaluator(permissions);
            }
            handler.setExpressionParser(bean.getExpressionParser());
            return handler;
        }

        private <T> T findInContext(Class<T> type) {
            if (BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
                    this.applicationContext, type).length == 1) {
                return this.applicationContext.getBean(type);
            }
            return null;
        }

    }

}
