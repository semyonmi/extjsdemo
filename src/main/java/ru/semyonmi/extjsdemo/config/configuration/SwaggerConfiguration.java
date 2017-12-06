package ru.semyonmi.extjsdemo.config.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ru.semyonmi.extjsdemo.config.properties.CoreProperties;
import ru.semyonmi.extjsdemo.controller.GlobalControllerExceptionHandler;
import ru.semyonmi.extjsdemo.security.EDClientDetails;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.ImplicitGrant;
import springfox.documentation.service.LoginEndpoint;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
@ComponentScan(basePackageClasses = {GlobalControllerExceptionHandler.class})
public class SwaggerConfiguration {

    private static final String VERSION_V1 = "v1";
    private static final String DEFAULT_INCLUDE_PATTERN = "/api/" + VERSION_V1 + "/.*";

    private static final String securitySchemaOAuth2 = "oauth2";
    private static final String authorizationScopeGlobalDesc ="access to api";

    @Autowired
    private CoreProperties coreProperties;
    @Autowired
    private EDClientDetails edClientDetails;

    @Bean
    public Docket swaggerSpringfoxDocketV1() {
        CoreProperties.Swagger swaggerProperties = coreProperties.getSwagger();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("/api/.*")
                .apiInfo(apiInfo())
                .enable(swaggerProperties.isEnabled())
                .genericModelSubstitutes(ResponseEntity.class)
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .paths(regex(DEFAULT_INCLUDE_PATTERN))
                .build()
                .securitySchemes(securitySchema())
                .securityContexts(securityContext());
        return docket;
    }

    private List<OAuth> securitySchema() {
        final LoginEndpoint loginEndpoint = new LoginEndpoint("./oauth/authorize/");
        final GrantType grantType = new ImplicitGrant(loginEndpoint, "access_token");
        return Collections.singletonList(new OAuth(securitySchemaOAuth2,
                Arrays.asList(getScope()),
                Collections.singletonList(grantType)));
    }

    private List<SecurityContext> securityContext() {
        return Collections.singletonList(SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(regex(DEFAULT_INCLUDE_PATTERN))
                .build());
    }

    private List<SecurityReference> defaultAuth() {
        return Collections.singletonList(new SecurityReference(securitySchemaOAuth2, getScope()));
    }

    @Bean
    public SecurityConfiguration securityInfo() {
        return new SecurityConfiguration(edClientDetails.getClientId(), edClientDetails.getClientSecret(), "realm",  "ExtJsDemo", null, ApiKeyVehicle.HEADER, "api_key", ",");
    }

    private ApiInfo apiInfo() {
        CoreProperties.Swagger swaggerProperties = coreProperties.getSwagger();
        CoreProperties.Swagger.Contact contactProperties = swaggerProperties.getContact();

        Contact contact = new Contact(contactProperties.getName(), contactProperties.getUrl(), contactProperties.getEmail());
        ApiInfo apiInfo = new ApiInfo(
                swaggerProperties.getTitle(),
                swaggerProperties.getDescription(),
                swaggerProperties.getVersion(),
                null,
                contact,
                null,
                null);
        return apiInfo;
    }

    private AuthorizationScope[] getScope() {
        AuthorizationScope[] authorizationScope = new AuthorizationScope[edClientDetails.getScope().size()];
        int i = 0;
        for (String scope : edClientDetails.getScope()) {
            authorizationScope[i] = new AuthorizationScope(scope, authorizationScopeGlobalDesc);
            i++;
        }
        return authorizationScope;
    }
}
