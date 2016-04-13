package fr.iocean.framework.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbokSecurityApplication {

    public static final String API_ROOT_PATH = "/api";
    
    public static void main(String[] args) {
        SpringApplication.run(SpringbokSecurityApplication.class, args);
    }
}
