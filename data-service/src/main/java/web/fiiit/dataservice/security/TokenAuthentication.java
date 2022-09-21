package web.fiiit.dataservice.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import web.fiiit.dataservice.document.Token;

import java.util.*;


public class TokenAuthentication implements Authentication {

    private final List<GrantedAuthority> authorities;

    private final String credentials;

    private final Object principal;

    private boolean authenticated = false;

    public TokenAuthentication(String token) {
        authorities = AuthorityUtils.NO_AUTHORITIES;
        credentials = token;
        principal = token;
    }

    public TokenAuthentication(
            String credentials,
            Token token,
            Collection<? extends GrantedAuthority> authorities
    ) {
        if (authorities == null) {
            this.authorities = AuthorityUtils.NO_AUTHORITIES;
        } else {
            this.authorities = Collections.unmodifiableList(new ArrayList<>(authorities));
            authenticated = true;
        }
        this.credentials = credentials;
        this.principal = token;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return credentials;
    }
}
