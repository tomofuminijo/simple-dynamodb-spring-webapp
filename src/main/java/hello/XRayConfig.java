package hello;

import javax.servlet.Filter;
import java.util.Collections;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.plugins.EC2Plugin;
import com.amazonaws.xray.plugins.ElasticBeanstalkPlugin;
import com.amazonaws.xray.strategy.sampling.LocalizedSamplingStrategy;
import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;

@Configuration
public class XRayConfig {
    static {
		

        AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard().withPlugin(new EC2Plugin()).withPlugin(new ElasticBeanstalkPlugin());
        
//        URL ruleFile = WebConfig.class.getResource("/sampling-rules.json");
//        builder.withSamplingStrategy(new LocalizedSamplingStrategy(ruleFile));
        
        AWSXRay.setGlobalRecorder(builder.build());

    }
  
    @Bean
    public Filter TracingFilter() {
        return new AWSXRayServletFilter("greeting");
    }
}