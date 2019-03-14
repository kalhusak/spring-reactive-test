package com.gft.reactivetest.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import reactor.core.publisher.Mono;

public class CustomJwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtReactiveAuthenticationManager authenticationManager;
    private final JwtAuthenticationConverter converter;

    public CustomJwtReactiveAuthenticationManager(ReactiveJwtDecoder jwtDecoder) {
        this.authenticationManager = new JwtReactiveAuthenticationManager(jwtDecoder);
        this.converter = new JwtAuthenticationConverter();
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return authenticationManager
                .authenticate(authentication)
                .cast(JwtAuthenticationToken.class)
                .map(converter::convert);
    }
}