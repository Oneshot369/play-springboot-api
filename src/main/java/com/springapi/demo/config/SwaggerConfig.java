package com.springapi.demo.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name = "Joshua Peck"
        ),
        description = "Personal Website API documentation",
        title = "Joshua's Capstone API",
        version = "0.1"
    ),
    servers = {
        @Server(
            description = "local ENV",
            url = "http://localhost:8080"
        ),
        @Server(
            description = "Prod",
            url = "http://Testbackend-env.eba-sqc4mqhu.us-east-2.elasticbeanstalk.com:8080"
        )
        
    }
)
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "BasicAuth", scheme = "bearer", bearerFormat= "JWT")

public class SwaggerConfig {
    
}
