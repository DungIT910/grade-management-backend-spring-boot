package com.boolfly.grademanagementrestful.config;

import com.boolfly.grademanagementrestful.domain.model.role.RoleModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final String ROLE_ADMIN = RoleModel.ROLE_ADMIN.getRoleName();
    private final String ROLE_LECTURER = RoleModel.ROLE_LECTURER.getRoleName();
    private final String ROLE_STUDENT = RoleModel.ROLE_STUDENT.getRoleName();
    private final String[] searchAPIs = {
            "/api/v1/subcols/search", "/api/v1/courses/search", "/api/v1/subjects/search",
            "/api/v1/grades/search", "/api/v1/students/search", "/api/v1/lecturers/search",
            "/api/v1/forums/search", "/api/v1/posts/search"
    };

    @Value("${jwt.signer-key}")
    private String signerKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/token").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/students").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/students").hasAnyAuthority(ROLE_ADMIN, ROLE_STUDENT)
                                .requestMatchers(HttpMethod.GET, "/api/v1/students/{studentId}").hasAnyAuthority(ROLE_ADMIN, ROLE_STUDENT)
                                .requestMatchers(HttpMethod.PUT, "/api/v1/lecturers").hasAnyAuthority(ROLE_ADMIN, ROLE_LECTURER)
                                .requestMatchers(HttpMethod.GET, "/api/v1/lecturers/{lecturerId}").hasAnyAuthority(ROLE_ADMIN, ROLE_LECTURER)
                                .requestMatchers("/api/v1/courses/{courseId}/subcols").authenticated()
                                .requestMatchers("/api/v1/courses/{courseId}/student-deactivation/{studentId}").hasAnyAuthority(ROLE_ADMIN, ROLE_LECTURER)
//                        .requestMatchers(searchAPIs).hasAuthority(ROLE_ADMIN)
                                .requestMatchers("/api/v1/courses/**").hasAuthority(ROLE_ADMIN)
                                .requestMatchers("/api/v1/grades/**").hasAnyAuthority(ROLE_ADMIN, ROLE_LECTURER)
                                .requestMatchers("/api/v1/forums/**").hasAnyAuthority(ROLE_ADMIN, ROLE_LECTURER)
                                .requestMatchers("/api/v1/subcols/**").hasAnyAuthority(ROLE_ADMIN, ROLE_LECTURER)
                                .requestMatchers("/api/v1/lecturers/**").hasAuthority(ROLE_ADMIN)
                                .requestMatchers("/api/v1/subjects/**").hasAuthority(ROLE_ADMIN)
                                .anyRequest().authenticated()
        );

        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())));

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("role");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
