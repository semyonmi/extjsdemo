package ru.semyonmi.extjsdemo.security.expression;

import org.springframework.context.ApplicationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.FilterInvocation;

public class EDOAuth2WebSecurityExpressionHandler extends OAuth2WebSecurityExpressionHandler {

    private ApplicationContext applicationContext;

    public EDOAuth2WebSecurityExpressionHandler() {
        super();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        super.setApplicationContext(applicationContext);
        this.applicationContext = applicationContext;
    }

    @Override
    protected StandardEvaluationContext createEvaluationContextInternal(Authentication authentication,
                                                                        FilterInvocation invocation) {
        StandardEvaluationContext ec = super.createEvaluationContextInternal(authentication, invocation);
        ec.setVariable("oauth2", new EDOAuth2SecurityExpressionMethods(applicationContext, authentication));
        return ec;
    }
}
