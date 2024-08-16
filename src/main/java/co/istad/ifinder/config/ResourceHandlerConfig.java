package co.istad.ifinder.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceHandlerConfig implements WebMvcConfigurer {

    @Value("${media.server-path}")
    private String serverPath;

    @Value("${media.client-path}")
    private String clientPath;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000","http://localhost:3001","http://localhost:3002","http://34.1.193.19:3005/","http://34.1.193.19:3006/","https://ifinder.sopheaktra.xyz/","https://ifinder.sopheaktra.xyz", "https://ifinder.istad.co","https://ifinder-admin.istad.co")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(clientPath)
                .addResourceLocations("file:" + serverPath)
                .addResourceLocations("classpath:/static/");
    }
}
