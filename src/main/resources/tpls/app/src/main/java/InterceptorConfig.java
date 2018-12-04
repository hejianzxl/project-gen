package ${groupId}.config;

import ${groupId}.filter.CrosIntercept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author luiz
 * @Title: InterceptorConfig
 * @ProjectName ${artifactId}-springboot
 * @Description: TODO
 * @date 2018/11/22 17:15
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    CrosIntercept crosIntercept;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(crosIntercept).addPathPatterns("/**");
    }

}
