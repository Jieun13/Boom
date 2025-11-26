package me.jiny.boom.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("로컬 서버");

        Info info = new Info()
            .title("Boom! API")
            .description("'요즘 나의 붐은?' 취향 아카이브 서비스 API")
            .version("v1.0")
            .contact(new Contact()
                .name("Boom! Team")
                .email("contact@boom.com"))
            .license(new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html"));

        SecurityScheme bearerAuth = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .name("Authorization");

        return new OpenAPI()
            .info(info)
            .servers(List.of(server))
            .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes("bearerAuth", bearerAuth));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("v1")
            .pathsToMatch("/api/**")
            .packagesToScan("me.jiny.boom.controller")
            .packagesToExclude("me.jiny.boom.domain.entity", "me.jiny.boom.domain")
            .build();
    }
}
