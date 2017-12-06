package ru.semyonmi.extjsdemo.config.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import ru.semyonmi.extjsdemo.security.EDAuthenticationProvider;
import ru.semyonmi.extjsdemo.security.EDClientDetails;
import ru.semyonmi.extjsdemo.security.EDClientDetailsService;
import ru.semyonmi.extjsdemo.security.EDUserApprovalHandler;
import ru.semyonmi.extjsdemo.security.JsonOAuth2ExceptionRenderer;
import ru.semyonmi.extjsdemo.security.expression.EDOAuth2WebSecurityExpressionHandler;

@Configuration
public class OAuth2ServerConfigurer {
    public static final String RESOURCE_ID = "API";

    @Autowired
    private EDClientDetails EDClientDetails;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private OAuth2RequestFactory oauth2RequestFactory;
    @Autowired
    private ApprovalStore approvalStore;
    @Autowired
    private UserApprovalHandler userApprovalHandler;
    @Autowired
    private OAuth2AuthenticationEntryPoint oauth2AuthenticationEntryPoint;

    @Bean
    public OAuth2AuthenticationEntryPoint oauth2AuthenticationEntryPoint() {
        OAuth2AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        authenticationEntryPoint.setRealmName(RESOURCE_ID);
        authenticationEntryPoint.setExceptionRenderer(new JsonOAuth2ExceptionRenderer());
        return authenticationEntryPoint;
    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    public ClientDetailsService clientDetailsService() {
        EDClientDetailsService clientDetailsService = new EDClientDetailsService();
        clientDetailsService.setClientDetails(EDClientDetails);
        return clientDetailsService;
    }

    @Bean
    public UserApprovalHandler userApprovalHandler() {
        EDUserApprovalHandler userApprovalHandler = new EDUserApprovalHandler();
        userApprovalHandler.setApprovalStore(approvalStore);
        userApprovalHandler.setRequestFactory(oauth2RequestFactory);
        userApprovalHandler.setClientDetailsService(clientDetailsService);
        return userApprovalHandler;
    }
    @Bean
    public ApprovalStore approvalStore() {
        TokenApprovalStore approvalStore = new TokenApprovalStore();
        approvalStore.setTokenStore(tokenStore);
        return approvalStore;
    }
    @Bean
    public OAuth2RequestFactory oauth2RequestFactory() {
        return new DefaultOAuth2RequestFactory(clientDetailsService);
    }


    @Configuration
    @EnableResourceServer
    protected class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Autowired
        private OAuth2WebSecurityExpressionHandler expressionHandler;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId(RESOURCE_ID).stateless(false)
                    .tokenServices(tokenServices());
            resources.expressionHandler(expressionHandler);
        }

        @Bean
        public OAuth2WebSecurityExpressionHandler expressionHandler(ApplicationContext applicationContext) {
            OAuth2WebSecurityExpressionHandler expressionHandler = new EDOAuth2WebSecurityExpressionHandler();
            expressionHandler.setApplicationContext(applicationContext);
            return expressionHandler;
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .and()
                .authorizeRequests()
                    .antMatchers("/api/**")
                    .access("#oauth2.isOAuth() and #oauth2.hasScope('api')")
            .and().exceptionHandling().authenticationEntryPoint(oauth2AuthenticationEntryPoint)
            .and().csrf().disable();

            // @formatter:on
            http.headers().frameOptions().disable();
        }

        DefaultTokenServices tokenServices() {
            DefaultTokenServices tokenService = new DefaultTokenServices();
            tokenService.setTokenStore(tokenStore);
            tokenService.setSupportRefreshToken(true);
            tokenService.setClientDetailsService(clientDetailsService);
            return tokenService;
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;
        @Autowired
        private EDAuthenticationProvider authenticationProviderBean;

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.withClientDetails(clientDetailsService);
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler)
                    .authenticationManager(authenticationManager);
        }
    }
}