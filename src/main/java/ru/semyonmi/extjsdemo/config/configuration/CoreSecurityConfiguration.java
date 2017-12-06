package ru.semyonmi.extjsdemo.config.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ru.semyonmi.extjsdemo.security.EDAuthenticationEntryPoint;
import ru.semyonmi.extjsdemo.security.EDAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CoreSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private ApplicationContext appContext;

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
      .antMatchers("/swagger-ui**")
      .antMatchers("/webjars/**");
  }

  @Autowired
  private EDAuthenticationEntryPoint authenticationEntryPoint;
  @Autowired
  private EDAuthenticationProvider authenticationProviderBean;

  @Autowired
  public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProviderBean);
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public EDAuthenticationProvider authenticationProviderBean() {
    return new EDAuthenticationProvider();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .antMatchers("/oauth/**").fullyAuthenticated()
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .httpBasic().authenticationEntryPoint(authenticationEntryPoint)
    .and()
        .headers().httpStrictTransportSecurity();
    http.csrf().disable();
    http.headers().frameOptions().disable();
  }

  @Bean
  public EDAuthenticationEntryPoint authenticationEntryPoint() {
    return new EDAuthenticationEntryPoint();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
