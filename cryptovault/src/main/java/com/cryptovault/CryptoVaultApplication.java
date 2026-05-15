package com.cryptovault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.cryptovault")
public class CryptoVaultApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoVaultApplication.class, args);
        System.out.println(" CryptoVault Web Application Started Successfully!");
        System.out.println(" Open your browser and go to: http://localhost:8080");
    }
}