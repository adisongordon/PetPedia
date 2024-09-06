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
                    registry.requestMatchers("/req/sign-up", "/css/**", "/js/**", "/fragments/**", "/home").permitAll();
                    registry.anyRequest().authenticated();
                })
                .build();

//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(registry -> {
//                    registry.requestMatchers("/home", "/login/**", "/sign-up/**").permitAll();
//                    registry.requestMatchers("/admin/**").hasRole(Role.ADMIN.name());
//                    registry.requestMatchers("/user/**").hasRole(Role.USER.name());
//                    registry.anyRequest().authenticated();
//                })
//                .formLogin(httpSecurityFormLoginConfigurer -> {
//                    httpSecurityFormLoginConfigurer
//                            .loginPage("/login")
//                            .defaultSuccessUrl("/", true)
//                            .permitAll();
//                })
//                .logout((logoutConfig) ->
//                        logoutConfig.logoutSuccessUrl("/")
//                )
//                .userDetailsService(usersDetailsService);
//        return http.getOrBuild();
    }
}
