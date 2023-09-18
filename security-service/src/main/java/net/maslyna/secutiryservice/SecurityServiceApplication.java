package net.maslyna.secutiryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SecurityServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityServiceApplication.class, args);
    }
}
