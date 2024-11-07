package com.amateuraces.security;

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

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    
    private UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailService){
        this.userDetailsService = userDetailService;
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
//                .requestMatchers(HttpMethod.PUT, "/players/*").hasAnyRole("ADMIN","USER")
//                .requestMatchers(HttpMethod.DELETE, "/players/*").authenticated()
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authz) -> authz
                .requestMatchers("/error").permitAll() // the default error page
                .requestMatchers(HttpMethod.GET, "/users", "/users/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/*/players").hasRole("USER")  
                .requestMatchers(HttpMethod.POST, "/users/*/admins").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/player/delete/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/players", "/api/players").permitAll()
                .requestMatchers(HttpMethod.GET, "/admins").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.PUT, "/users/*").hasAnyRole("USER","ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/users/*").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.GET, "/tournaments", "/tournaments/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/tournaments").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/tournaments/*").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/tournaments/*").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/tournaments/*/players/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/matches").permitAll()
                .requestMatchers(HttpMethod.GET, "/matches/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/matches").permitAll()//hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/matches/{id}").permitAll()//hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/matches/{id}/result").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/matches/{id}/result/updateScore").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/matches/{id}/result/updateWinner").hasAuthority("ROLE_ADMIN")


                .requestMatchers("/register", "/login", "/player/register/**").permitAll()
                .requestMatchers("/home").permitAll()  // home is accessible to those with registered accounts
                .requestMatchers("/h2-console/**").permitAll()  // Allow access to H2 Console
                // note that Spring Security 6 secures all endpoints by default
                // remove the below line after adding the required rules
                // .anyRequest().permitAll()  // All other requests require authentication
            )
            // ensure that the application won’t create any session in our stateless REST APIs
            .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(Customizer.withDefaults())

            .formLogin(form -> form
                .loginPage("/login")  // Custom login page
                .defaultSuccessUrl("/home", true)  // Redirect to home after successful login
                .failureUrl("/login?error=true")  // Redirect to login page with error=true on failure
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login") // Redirect to login after logout
                .permitAll()

            )
            .csrf(csrf -> csrf.disable()) // CSRF protection is needed only for browser based attacks

            .headers(header -> header.disable()) // disable the security headers, as we do not return HTML in our APIs
            .authenticationProvider(authenticationProvider());

        return http.build();
    }



    /**
     * @Bean annotation is used to declare a PasswordEncoder bean in the Spring application context. 
     * Any calls to encoder() will then be intercepted to return the bean instance.
     */
    @Bean
    public BCryptPasswordEncoder encoder() {
        // auto-generate a random salt internally
        return new BCryptPasswordEncoder();
    }
}
 