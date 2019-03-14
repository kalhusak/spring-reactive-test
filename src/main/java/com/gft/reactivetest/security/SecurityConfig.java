package com.gft.reactivetest.security;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .authorizeExchange()
                .anyExchange().permitAll()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .authenticationManager(new CustomJwtReactiveAuthenticationManager(reactiveJwtDecoder()));
        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        RSAPublicKey publicKey = getPublicKey();
        return new NimbusReactiveJwtDecoder(publicKey);
    }

    private RSAPublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        InputStream inputStream = new ClassPathResource("auth_public_key.txt").getInputStream();
        String publicKey = IOUtils.toString(inputStream, "UTF-8");
        publicKey = publicKey.replaceAll("\n", "").replaceAll("\r", "");
        byte[] bytes = Base64.getDecoder().decode(publicKey);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));
    }
}
