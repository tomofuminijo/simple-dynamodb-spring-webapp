package hello;

import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Collections;

@Configuration
public class XRayConfig {
    
    @Bean
    public FilterRegistrationBean awsxRayServletFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new AWSXRayServletFilter("xray-sample"));
        bean.setUrlPatterns(Collections.singleton("/*"));
        return bean;
    }
}