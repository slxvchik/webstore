package com.webstore.auth_service.config;

import com.webstore.auth_service.jwt.JwtFilter;
import com.webstore.auth_service.jwt.JwtUtils;
import com.webstore.auth_service.jwt.blacklist.JwtBlacklistService;
import com.webstore.auth_service.user.UserDetailService;
import com.webstore.auth_service.user.deleted.DeletedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private final UserDetailService userDetailService;
    private final JwtBlacklistService jwtBlacklistService;
    private final DeletedUserService deletedUserService;

    @Autowired
    public SecurityConfig(JwtUtils jwtUtils, UserDetailService userDetailService, JwtBlacklistService jwtBlacklistService, DeletedUserService deletedUserService) {
        this.jwtUtils = jwtUtils;
        this.userDetailService = userDetailService;
        this.jwtBlacklistService = jwtBlacklistService;
        this.deletedUserService = deletedUserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/password/forgot").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/password/recovery/**").permitAll()
                        .anyRequest().authenticated()
                )
                .rememberMe(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtFilter(jwtUtils, userDetailService, jwtBlacklistService, deletedUserService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
