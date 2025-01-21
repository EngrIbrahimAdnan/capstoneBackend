package com.fullstackbootcamp.capstoneBackend.auth.config;

import com.fullstackbootcamp.capstoneBackend.auth.util.JwtKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtKeyGenerator jwtKeyGenerator;

    public SecurityConfig(JwtKeyGenerator jwtKeyGenerator) {
        this.jwtKeyGenerator = jwtKeyGenerator;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10); // Recommended strength
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() //Disabling CSRF is fine for APIs, but if you plan to support web clients in the future, consider adding CSRF tokens for form submissions
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/setup/**").permitAll()

//                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // uncomment if you need endpoints for only admin
//                        .requestMatchers("/api/user/**").hasRole("USER") // uncomment if you need endpoints for only user
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
                        .decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
                ));

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(jwtKeyGenerator.getSecretKey()).build();
    }

    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles"); // Match JWT claim
        grantedAuthoritiesConverter.setAuthorityPrefix(""); // No need for "ROLE_" here

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return authenticationConverter;
    }
}
