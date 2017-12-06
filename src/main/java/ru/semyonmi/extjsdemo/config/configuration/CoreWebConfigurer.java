package ru.semyonmi.extjsdemo.config.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

import ru.semyonmi.extjsdemo.config.properties.CoreProperties;

@Configuration
@EnableWebMvc
public class CoreWebConfigurer extends WebMvcConfigurerAdapter {

  @Autowired
  private CoreProperties coreProperties;
  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    super.configureMessageConverters(converters);
    MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
    messageConverter.setObjectMapper(objectMapper);
    converters.add(messageConverter);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/swagger-ui**").addResourceLocations("classpath:/META-INF/resources/");
    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addRedirectViewController("/", "index.html");
    registry.addRedirectViewController("/index", "index.html");
  }

  //avoiding problem with dot in @PathVariable, e.g. /somepath/{variable:.+}
  //http://stackoverflow.com/q/16332092/579680
  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.setUseRegisteredSuffixPatternMatch(true);
  }

  @Bean
  public ServletRegistrationBean h2servletRegistration(){
    ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
    registrationBean.addUrlMappings("/console/*");
    return registrationBean;
  }
}
