package com.booking.accommodations.booking.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> auth.requestMatchers("/v1/order").permitAll()
                                               .requestMatchers("/v1/offers").permitAll()
                                               .requestMatchers(
                                                       "/swagger-ui/**",
                                                       "/swagger-ui.html",
                                                       "/v3/api-docs/**",
                                                       "/openapi.yaml",
                                                       "/error/**"
                                               ).permitAll()
                                               .requestMatchers("/v1/unit/statistics").permitAll()
                                               .requestMatchers("/v1/unit/**").authenticated()
                                               .requestMatchers("/v1/units").authenticated()
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                               .username("user")
                               .password(passwordEncoder().encode("user@123"))
                               .roles("USER")
                               .build();
        UserDetails admin = User.builder()
                                .username("admin")
                                .password(passwordEncoder().encode("admin@123"))
                                .roles("ADMIN")
                                .build();
        return new InMemoryUserDetailsManager(admin, user);
    }
}
