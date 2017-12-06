package ru.semyonmi.extjsdemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import ru.semyonmi.extjsdemo.config.properties.CoreProperties;

@Component
public class EDClientDetails implements ClientDetails {

    @Autowired
    private CoreProperties coreProperties;

    @Override
    public String getClientId() {
        return coreProperties.getOauthClient().getClientId();
    }

    @Override
    public Set<String> getResourceIds() {
        return coreProperties.getOauthClient().getResourceIds();
    }

    @Override
    public boolean isSecretRequired() {
        return coreProperties.getOauthClient().isSecretRequired();
    }

    @Override
    public String getClientSecret() {
        return coreProperties.getOauthClient().getClientSecret();
    }

    @Override
    public boolean isScoped() {
        return coreProperties.getOauthClient().isScoped();
    }

    @Override
    public Set<String> getScope() {
        return coreProperties.getOauthClient().getScope();
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return coreProperties.getOauthClient().getAuthorizedGrantTypes();
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return coreProperties.getOauthClient().getRegisteredRedirectUri();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return coreProperties.getOauthClient().getAuthorities();
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return coreProperties.getOauthClient().getAccessTokenValiditySeconds();
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return coreProperties.getOauthClient().getRefreshTokenValiditySeconds();
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return coreProperties.getOauthClient().isAutoApprove();
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }
}
