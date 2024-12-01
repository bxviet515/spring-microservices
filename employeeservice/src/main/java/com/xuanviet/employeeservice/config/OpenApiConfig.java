package com.xuanviet.employeeservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(

        info = @Info(
                title = "Employee Api Specification - Xuan Viet",
                description = "Api documentation for Employee Service",
                version = "1.0",
                contact = @Contact(
                        name = "Xuan Viet",
                        email = "vietsdk@gmail.com",
                        url = "https://google.com"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://google.com"
                ),
                termsOfService = "https://google.com"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:9002"

                ),
                @Server(
                        description = "Dev ENV",
                        url = "https://google.com"
                ),
                @Server(
                        description = "Dev ENV",
                        url = "https://facebook.com"
                ),

        }
)
public class OpenApiConfig {

}
