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
    
    private UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }
    
    /**
     * Exposes a bean of DaoAuthenticationProvider, a type of AuthenticationProvider
     * Attaches the user details and the password encoder   
     * @return
     */

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
            .cors(Customizer.withDefaults()) // Enable CORS using the custom configuration source
            .authorizeHttpRequests((authz) -> authz
                .requestMatchers("/error").permitAll() // the default error page
                .requestMatchers(HttpMethod.GET, "/users", "/users/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/*/players").hasRole("USER")  
                .requestMatchers(HttpMethod.POST, "/users/*/admins").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/player/delete/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/players").permitAll()
                .requestMatchers(HttpMethod.GET, "/admins").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.PUT, "/users/*").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/users/*").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.GET, "/tournaments", "/tournaments/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/tournaments").permitAll()
                .requestMatchers(HttpMethod.PUT, "/tournaments/*").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/tournaments/*").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/tournaments/*/players/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/tournaments/*/matches").permitAll()
                .requestMatchers(HttpMethod.GET, "/tournaments/*/matches/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/tournaments/*/matches").permitAll() // hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/tournaments/*/matches/{id}").permitAll() // hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/tournaments/*/matches/{id}/result/*").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/h2-console/**").permitAll() // Allow access to H2 Console
                .anyRequest().permitAll()  // All other requests require authentication
            )
            .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless for REST APIs
            .httpBasic(Customizer.withDefaults()) // Basic HTTP authentication for stateless APIs (JWT or similar auth will be used in headers)
            .csrf(csrf -> csrf.disable()) // CSRF protection is disabled for REST APIs
            .headers(headers -> headers.disable()) // Disable headers since this is for API usage
            .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Allow only React frontend URL
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // Allow credentials such as cookies, authorization headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * @Bean annotation is used to declare a PasswordEncoder bean in the Spring application context. 
     * Any calls to encoder() will then be intercepted to return the bean instance.
     */
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder(); // Password encoder for hashing passwords
    }
}
