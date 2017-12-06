package ru.semyonmi.extjsdemo.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@ConfigurationProperties(prefix = "extjsdemo", ignoreUnknownFields = false)
public class CoreProperties {

  private String version;
  private final OAuthClient oauthClient = new OAuthClient();
  private String oauthUserName;
  private String oauthPassword;
  private final Pool pool = new Pool();
  private final Swagger swagger = new Swagger();

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public OAuthClient getOauthClient() {
    return oauthClient;
  }

  public String getOauthUserName() {
    return oauthUserName;
  }

  public void setOauthUserName(String oauthUserName) {
    this.oauthUserName = oauthUserName;
  }

  public String getOauthPassword() {
    return oauthPassword;
  }

  public void setOauthPassword(String oauthPassword) {
    this.oauthPassword = oauthPassword;
  }

  public Pool getPool() {
    return pool;
  }

  public Swagger getSwagger() {
    return swagger;
  }

  public static class Pool {

    private int connectionTimeout = 30000; // millis
    private int idleTimeout = 600000; // millis
    private int maximumPoolSize = 100;

    public int getConnectionTimeout() {
      return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
      this.connectionTimeout = connectionTimeout;
    }

    public int getIdleTimeout() {
      return idleTimeout;
    }

    public void setIdleTimeout(int idleTimeout) {
      this.idleTimeout = idleTimeout;
    }

    public int getMaximumPoolSize() {
      return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
      this.maximumPoolSize = maximumPoolSize;
    }
  }

  public static class OAuthClient {
    private String clientId;
    private Set<String> resourceIds;
    private boolean secretRequired;
    private String clientSecret;
    private boolean scoped;
    private Set<String> scope;
    private Set<String> authorizedGrantTypes;
    private Set<String> registeredRedirectUri;
    private Collection<GrantedAuthority> authorities;
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;
    private boolean autoApprove = true;

    public String getClientId() {
      return clientId;
    }

    public void setClientId(String clientId) {
      this.clientId = clientId;
    }

    public Set<String> getResourceIds() {
      return resourceIds;
    }

    public void setResourceIds(Set<String> resourceIds) {
      this.resourceIds = resourceIds;
    }

    public boolean isSecretRequired() {
      return secretRequired;
    }

    public void setSecretRequired(boolean secretRequired) {
      this.secretRequired = secretRequired;
    }

    public String getClientSecret() {
      return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
      this.clientSecret = clientSecret;
    }

    public boolean isScoped() {
      return scoped;
    }

    public void setScoped(boolean scoped) {
      this.scoped = scoped;
    }

    public Set<String> getScope() {
      return scope;
    }

    public void setScope(Set<String> scope) {
      this.scope = scope;
    }

    public Set<String> getAuthorizedGrantTypes() {
      return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(Set<String> authorizedGrantTypes) {
      this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public Set<String> getRegisteredRedirectUri() {
      return registeredRedirectUri == null ? Collections.emptySet() : registeredRedirectUri;
    }

    public void setRegisteredRedirectUri(Set<String> registeredRedirectUri) {
      this.registeredRedirectUri = registeredRedirectUri;
    }

    public Collection<GrantedAuthority> getAuthorities() {
      return authorities;
    }

    public void setAuthorities(String... authorities) {
      this.authorities = AuthorityUtils.createAuthorityList(authorities);
    }

    public Integer getAccessTokenValiditySeconds() {
      return accessTokenValiditySeconds;
    }

    public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
      this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    public Integer getRefreshTokenValiditySeconds() {
      return refreshTokenValiditySeconds;
    }

    public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
      this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    public boolean isAutoApprove() {
      return autoApprove;
    }

    public void setAutoApprove(boolean autoApprove) {
      this.autoApprove = autoApprove;
    }
  }

  public static class Swagger {

    private String title = "API";

    private String description = "API documentation";

    private String version = "v1";

    private String termsOfServiceUrl;

    private String license;

    private String licenseUrl;

    private boolean enabled;

    private Contact contact = new Contact();

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getVersion() {
      return version;
    }

    public void setVersion(String version) {
      this.version = version;
    }

    public String getTermsOfServiceUrl() {
      return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
      this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public Contact getContact() {
      return contact;
    }

    public void setContact(Contact contact) {
      this.contact = contact;
    }

    public String getLicense() {
      return license;
    }

    public void setLicense(String license) {
      this.license = license;
    }

    public String getLicenseUrl() {
      return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
      this.licenseUrl = licenseUrl;
    }

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public static class Contact {

      private String name;
      private String url;
      private String email;

      public String getName() {
        return name;
      }

      public void setName(String name) {
        this.name = name;
      }

      public String getUrl() {
        return url;
      }

      public void setUrl(String url) {
        this.url = url;
      }

      public String getEmail() {
        return email;
      }

      public void setEmail(String email) {
        this.email = email;
      }
    }
  }
}
