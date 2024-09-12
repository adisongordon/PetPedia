package com.petpedia.web.config;

import com.petpedia.web.model.UsersDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecSecurityConfig {

    @Autowired
    private final UsersDetailsService usersDetailsService;

    @Bean
    public UsersDetailsService userDetailsService(){
        return usersDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usersDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(httpForm -> {
                    httpForm.loginPage("/req/login").permitAll();
                    httpForm.defaultSuccessUrl("/home");
                })
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/css/**", "/js/**", "/fragments/**", "/images/**").permitAll();
                    registry.requestMatchers("/").permitAll();
                    registry.requestMatchers("/wiki").permitAll();
                    registry.requestMatchers("/quiz").permitAll();
                    registry.requestMatchers("/forum").permitAll();
                    registry.requestMatchers("/shelters").permitAll();
                    registry.requestMatchers("/shelters/RuffHaven").permitAll();
                    registry.requestMatchers("/map").permitAll();
                    registry.requestMatchers("/req/signup").permitAll();
                    registry.anyRequest().authenticated();
                })
                .logout(config -> config.logoutSuccessUrl("/"))
                .build();

    }
}
