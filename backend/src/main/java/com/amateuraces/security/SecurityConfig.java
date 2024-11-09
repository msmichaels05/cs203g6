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

    public SecurityConfig(UserDetailsService userDetailService){
        this.userDetailsService = userDetailService;
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
            .cors(Customizer.withDefaults()) // Enable CORS using the custom configuration source
            .authorizeHttpRequests((authz) -> authz
                // the default error page
                .requestMatchers("/error").permitAll()
                
                // Users security
                .requestMatchers(HttpMethod.GET, "/users", "/users/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.PUT, "/users/*").hasAnyRole("USER","ADMIN")
                // Players security
                .requestMatchers(HttpMethod.GET, "/players").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/*/players").hasRole("USER")  

                // Admin security
                .requestMatchers(HttpMethod.GET, "/admins").permitAll()

                // Tournament security
                .requestMatchers(HttpMethod.GET, "/tournaments", "/tournaments/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/tournaments").permitAll()
                .requestMatchers(HttpMethod.PUT, "/tournaments/*").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/tournaments/*").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/tournaments/*/players/*").permitAll()



                // .requestMatchers("/register", "/login", "/player/register/**").permitAll()
                // .requestMatchers("/home").permitAll()  // home is accessible to those with registered accounts
                .requestMatchers("/h2-console/**").permitAll()  // Allow access to H2 Console
                // note that Spring Security 6 secures all endpoints by default
                // remove the below line after adding the required rules
                .anyRequest().permitAll()  // All other requests require authentication
            )
            // ensure that the application won’t create any session in our stateless REST APIs
            .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(Customizer.withDefaults())

            // CSRF protection is needed only for browser based attacks
            .csrf(csrf -> csrf.disable()) 

            // Disable headers, as we do not return HTML in our APIs
            .headers(headers -> headers.disable()) 
            .authenticationProvider(authenticationProvider());

        return http.build();
    }

    // For REACT
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow only your frontend origin
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); 
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        // auto-generate a random salt internally
        return new BCryptPasswordEncoder();
    }
}
 