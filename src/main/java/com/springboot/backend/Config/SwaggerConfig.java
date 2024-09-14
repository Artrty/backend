package com.springboot.backend.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//http://localhost:8080/swagger-ui/index.html 해당 링크로 입력 시 Swagger에 입력된 정보 확인 가능

@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Spring Boot REST API")
                .description("Specification")
                .version("1.0.0");
    }


}
