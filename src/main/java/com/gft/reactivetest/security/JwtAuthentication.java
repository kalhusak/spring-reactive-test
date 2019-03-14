package com.gft.reactivetest.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

@Getter
@EqualsAndHashCode(callSuper = true)
public class JwtAuthentication extends AbstractAuthenticationToken {

    private User user;
    private Jwt token;

    public JwtAuthentication(Jwt token, User user, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.user = user;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public String getName() {
        return token.getSubject();
    }
}
