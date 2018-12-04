package ${groupId}.config;

import ${groupId}.filter.CrosFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luiz
 * @Title: FilterConfig
 * @ProjectName ${artifactId}-springboot
 * @Description: TODO
 * @date 2018/11/22 16:57
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean registerFilter(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CrosFilter());
        registration.addUrlPatterns("/*");
        registration.setName("crosFilter");
        registration.setOrder(1);
        return registration;
    }
}
