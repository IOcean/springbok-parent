package fr.iocean.framework.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "fr.iocean")
@EntityScan(basePackages = "fr.iocean")
@EnableJpaRepositories("fr.iocean")
public class Application {
    
    public static final String API_ROOT_PATH = "/api";
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
