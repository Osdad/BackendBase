package com.unab.FacturaEnLinea;

import com.unab.FacturaEnLinea.security.JWTAuthorizationFilter;
import com.unab.FacturaEnLinea.service.IFileService;
import com.unab.FacturaEnLinea.service.impl.UserService;
 
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;

@SpringBootApplication
public class FacturaEnLineaApplication implements CommandLineRunner{

    @Resource
    IFileService fileService;

    public static void main(String[] args) {
        SpringApplication.run(FacturaEnLineaApplication.class, args);
    }

    @Override
    public void run(String... arg) throws Exception {
        //fileService.deleteAll();
       fileService.init();
    }

}
