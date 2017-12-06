package ru.semyonmi.extjsdemo.security.expression;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

public class EDOAuth2MethodSecurityExpressionHandler extends OAuth2MethodSecurityExpressionHandler {


    private ApplicationContext applicationContext;

    public EDOAuth2MethodSecurityExpressionHandler() {
        super();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        super.setApplicationContext(applicationContext);
        this.applicationContext = applicationContext;
    }

    @Override
    public StandardEvaluationContext createEvaluationContextInternal(Authentication authentication, MethodInvocation mi) {
        StandardEvaluationContext ec = super.createEvaluationContextInternal(authentication, mi);
        ec.setVariable("oauth2", new EDOAuth2SecurityExpressionMethods(applicationContext, authentication));
        return ec;
    }
}
