package com.bidhaa.configuration.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Value("${jwt.public.key}")
    private RSAPublicKey rsaPublicKey;

    @Value("${jwt.private.key}")
    private RSAPrivateKey rsaPrivateKey;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static final String[] AUTH_WHITE_LIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/v2/api-docs/**",
            "/swagger-resources/**",
            "/actuator/**",
            "/api/v1/users/login"
    };

    private static final String[] ADMIN_URL_LIST = {
            "/actuator/httpexchanges",
            "/api/v1/users/disable",
            "/api/v1/users/create"
    };
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

      try {

          http
                  .authorizeHttpRequests(authorize -> authorize
                          .requestMatchers(AUTH_WHITE_LIST)
                          .permitAll()
                          .requestMatchers(ADMIN_URL_LIST).hasRole("ADMIN")
                          .anyRequest()
                          .authenticated()
                  )
                  .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                  .httpBasic(Customizer.withDefaults())
                  .csrf(csrf -> csrf.disable())
                  .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                  .cors(cors -> cors.disable())
                  .userDetailsService(userDetailsService)
                  .passwordManagement(Customizer.withDefaults())
                  .exceptionHandling(
                          exceptions ->
                                  exceptions
                                          .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                          .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));

      }catch (Exception e){
          log.error("filter chain error",e);

      }

        return http.build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        var jwk = new RSAKey.Builder(this.rsaPublicKey).privateKey(this.rsaPrivateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    // Used by JwtAuthenticationProvider to decode and validate JWT tokens
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.rsaPublicKey).build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        //configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    @Bean
//    public RoleHierarchy roleHierarchy() {
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        String hierarchy = "ROLE_SUPERADMIN > ROLE_ADMIN \n ROLE_STAFF > ROLE_GUEST";
//        roleHierarchy.setHierarchy(hierarchy);
//        return roleHierarchy;
//    }
//
//    @Bean
//    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
//        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
//        expressionHandler.setRoleHierarchy(roleHierarchy());
//        return expressionHandler;
//    }
}
