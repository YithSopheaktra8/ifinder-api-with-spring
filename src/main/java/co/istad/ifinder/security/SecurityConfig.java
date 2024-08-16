package co.istad.ifinder.security;

import co.istad.ifinder.utils.KeyUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final KeyUtil keyUtil;

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;

    }

    @Bean
    JwtAuthenticationProvider jwtAuthenticationProvider(@Qualifier("refreshJwtDecoder") JwtDecoder refreshJwtDecoder) {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(refreshJwtDecoder);
        return provider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests(request -> request
                        .requestMatchers("api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"api/v1/auth/total-record").hasAnyAuthority("SCOPE_admin:read","SCOPE_admin:write")
                        .requestMatchers("api/v1/gemini/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"api/v1/search/multi_search").permitAll()
                        .requestMatchers(HttpMethod.POST,"api/v1/search/dataImport").hasAuthority("SCOPE_admin:write")
                        .requestMatchers(HttpMethod.POST,"api/v1/search/scrape").hasAuthority("SCOPE_admin:write")
                        .requestMatchers(HttpMethod.GET,"api/v1/users/").permitAll()
                        .requestMatchers(HttpMethod.GET,"api/v1/users/*/admins/**").hasAuthority("SCOPE_admin:read")
                        .requestMatchers(HttpMethod.PATCH,"api/v1/users/**").hasAnyAuthority("SCOPE_admin:write","SCOPE_user:write")
                        .requestMatchers(HttpMethod.PUT,"api/v1/users/**").hasAuthority("SCOPE_admin:write")
                        .requestMatchers(HttpMethod.POST,"api/v1/media/**").hasAnyAuthority("SCOPE_admin:write","SCOPE_user:write")
                        .requestMatchers("api/v1/web-scraper/**").hasAnyAuthority("SCOPE_admin:write","SCOPE_admin:read")
                        .requestMatchers(HttpMethod.GET,"api/v1/collection/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"api/v1/collection/**").hasAnyAuthority("SCOPE_admin:write","SCOPE_admin:read")
                        .requestMatchers(HttpMethod.PATCH,"api/v1/collection/**").hasAnyAuthority("SCOPE_admin:write","SCOPE_admin:read")
                        .requestMatchers(HttpMethod.PUT,"api/v1/collection/**").hasAnyAuthority("SCOPE_admin:write","SCOPE_admin:read")
                        .requestMatchers(HttpMethod.DELETE,"api/v1/collection/**").hasAnyAuthority("SCOPE_admin:write","SCOPE_admin:read")
                        .requestMatchers("api/v1/video/**").permitAll()
                        .requestMatchers("api/v1/user-history/**").hasAnyAuthority("SCOPE_admin:write","SCOPE_user:write","SCOPE_user:read","SCOPE_admin:read")
                        .requestMatchers("api/v1/folders/**").hasAnyAuthority("SCOPE_admin:write","SCOPE_user:write","SCOPE_user:read","SCOPE_admin:read")
                        .requestMatchers("api/v1/bookmarks/**").hasAnyAuthority("SCOPE_admin:write","SCOPE_user:write","SCOPE_user:read","SCOPE_admin:read")
                        .requestMatchers(HttpMethod.POST,"api/v1/feedback/**").hasAnyAuthority("SCOPE_user:write","SCOPE_user:read")
                        .requestMatchers(HttpMethod.GET,"api/v1/feedback/**").hasAnyAuthority("SCOPE_admin:write","SCOPE_admin:read")
                        .requestMatchers(HttpMethod.DELETE,"api/v1/feedback/**").hasAnyAuthority("SCOPE_admin:write","SCOPE_admin:read")
                        .requestMatchers(HttpMethod.GET,"api/v1/auth/total-record").hasAnyAuthority("SCOPE_admin:write","SCOPE_admin:read")
                        .anyRequest().permitAll()
                );
        // enable jwt security
        httpSecurity.oauth2ResourceServer(jwt -> jwt
                .jwt(Customizer.withDefaults()));

        // disable csrf for submit form because we develop api
        httpSecurity.csrf(token -> token.disable());

        // change from statefull to stateless because api use stateless
        httpSecurity.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }


    @Primary
    @Bean("jwkSource")
    JWKSource<SecurityContext> jwkSource(KeyUtil keyUtil) {

        RSAKey rsaKey = new RSAKey.Builder(keyUtil.getAccessTokenPublicKey())
                .keyID(UUID.randomUUID().toString())
                .privateKey(keyUtil.getAccessTokenPrivateKey())
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);

        return ((jwkSelector, securityContext) -> jwkSelector
                .select(jwkSet));
    }

    // issue access token or get access token
    @Primary
    @Bean("jwtEncoder")
    JwtEncoder jwtEncoder(@Qualifier("jwkSource") JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    // when client submit token from header to get the protected resource
    @Primary
    @Bean("jwtDecoder")
    JwtDecoder jwtDecoder(KeyUtil keyUtil) throws JOSEException {
        return NimbusJwtDecoder
                .withPublicKey(keyUtil.getAccessTokenPublicKey())
                .build();
    }

    @Bean("refreshJwkSource")
    JWKSource<SecurityContext> refreshJwkSource() {

        RSAKey rsaKey = new RSAKey.Builder(keyUtil.getRefreshTokenPublicKey())
                .keyID(UUID.randomUUID().toString())
                .privateKey(keyUtil.getRefreshTokenPrivateKey())
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);

        return ((jwkSelector, securityContext) -> jwkSelector
                .select(jwkSet));
    }

    // issue access token or get access token
    @Bean("refreshJwtEncoder")
    JwtEncoder refreshJwtEncoder(@Qualifier("refreshJwkSource") JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    // when client submit token from header or body to get the protected resource
    @Bean("refreshJwtDecoder")
    JwtDecoder refreshJwtDecoder() {
        return NimbusJwtDecoder
                .withPublicKey(keyUtil.getRefreshTokenPublicKey())
                .build();
    }

}
