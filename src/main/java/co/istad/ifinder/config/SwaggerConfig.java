package co.istad.ifinder.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class SwaggerConfig {

    @Value("${server.port}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
//                .components(new Components()
//                        .addSecuritySchemes("accessToken", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("iFinder API")
                        .version("1.0.0")
                        .description("API for iFinder"))
                .servers(new ArrayList<>() {{
//                    add(new Server().url("http://localhost:" + serverPort).description("Localhost"));
                    add(new Server().url("http://34.143.180.228:" + serverPort).description("stage"));
//                    add(new Server().url("http://35.213.166.167:" + serverPort).description("prod"));
                }});
    }

}
