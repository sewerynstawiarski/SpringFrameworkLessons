package com.seweryn.RestMvcProject.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("!test")
public class SpringSecConfig {
    @Bean
    @Order(1)
    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(EndpointRequest.toAnyEndpoint())
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
//                .authorizeHttpRequests(authorize -> authorize.requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()); //It's doing the same thing but in different way

        return http.build();
    }
    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers("/v3/api-docs**", "/swagger-ui/**" , "/swagger-ui.html")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
//                .httpBasic(Customizer.withDefaults())
//                .csrf(csrf -> {
//            csrf.ignoringRequestMatchers("/api/**");
//        });

        return http.build();
    }
}
