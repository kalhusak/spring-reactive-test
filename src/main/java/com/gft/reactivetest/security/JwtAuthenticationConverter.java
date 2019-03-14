package com.gft.reactivetest.security;

import net.minidev.json.JSONObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

public class JwtAuthenticationConverter implements Converter<JwtAuthenticationToken, JwtAuthentication> {

    @Override
    public JwtAuthentication convert(JwtAuthenticationToken jwtAuthenticationToken) {
        Jwt jwt = jwtAuthenticationToken.getToken();
        Collection<GrantedAuthority> authorities = jwtAuthenticationToken.getAuthorities();
        User user = extractUser(jwt);
        return new JwtAuthentication(jwt, user, authorities);
    }

    private User extractUser(Jwt jwt) {
        JSONObject user = (JSONObject) jwt.getClaims().get("user");
        return user == null
                ? null
                : new User(
                (Long) user.getAsNumber("id"),
                user.getAsString("username"),
                user.getAsString("type"));
    }
}
