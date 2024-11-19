package com.amateuraces.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    
    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())  // Use the CorsConfiguration bean below
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/error").permitAll()
                .requestMatchers("/login").permitAll()  
                .requestMatchers(HttpMethod.GET, "/users", "/users/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/*/players").hasAuthority("ROLE_USER")  
                .requestMatchers(HttpMethod.POST, "/users/*/admins").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/player/delete/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/players").permitAll()
                .requestMatchers(HttpMethod.GET, "/admins").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.PUT, "/users/*").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/users/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/tournaments", "/tournaments/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/tournaments").permitAll()
                .requestMatchers(HttpMethod.PUT, "/tournaments/*").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/tournaments/*").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/tournaments/*/players/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/tournaments/*/matches").permitAll()
                .requestMatchers(HttpMethod.GET, "/tournaments/*/matches/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/tournaments/*/matches").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/tournaments/*/matches/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/tournaments/*/matches/{id}/result/*").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/login").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().permitAll()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 
            .httpBasic(Customizer.withDefaults()) 
            .csrf(csrf -> csrf.disable()) 
            .headers(headers -> headers.disable()) 
            .authenticationProvider(authenticationProvider());
    
        return http.build();
    }

    @Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("https://cs203g6-tennis.vercel.app")); // Allow only React frontend URL
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true); // Allow credentials such as cookies, authorization headers
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder(); // Password encoder for hashing passwords
    }
}
